/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * [DevByteViewModel] is an [AndroidViewModel] designed to store and manage UI-related data in a
 * lifecycle conscious way. This allows data to survive configuration changes such as screen
 * rotations. In addition, background work such as fetching network results can continue through
 * configuration changes and deliver results after the new Fragment or Activity is available.
 *
 * @param application The [Application] that this viewmodel is attached to. It's safe to hold a
 * reference to applications across rotation since [Application] is never recreated during activity
 * or fragment lifecycle events.
 */
class DevByteViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * The data source this ViewModel will fetch results from.
     */
    private val videosRepository = VideosRepository(getDatabase(application))

    /**
     * A playlist of videos displayed on the screen. The lambda of an `Observer` added to it in the
     * `onActivityCreated` override of `DevByteFragment` sets the `videos` field of the
     * `DevByteAdapter` used to feed data to its `RecyclerView` if it is not `null`, and our method
     * [refreshDataFromRepository] tests if to see if it is `null` or empty when it catches
     * [IOException] and sets the value of our [_eventNetworkError] property to `true` if it is.
     * It is also read by the binding expression for the "app:playlist" attribute of the
     * `ProgressBar` with ID R.id.loading_spinner in the layout file layout/fragment_dev_byte.xml
     * ("app:playlist" is one of two attributes used for the BindingAdapter `hideIfNetworkError`,
     * and the other is "app:isNetworkError" and both are used for that `ProgressBar`).
     * `hideIfNetworkError` sets the visibility of the `View` with the attribute "app:playlist" to
     * GONE if [playlist] transitions to `null`.
     */
    val playlist: LiveData<List<DevByteVideo>> = videosRepository.videos

    /**
     * This is the job for all coroutines started using our [CoroutineScope] field [viewModelScope]
     * by this ViewModel. Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob: CompletableJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by [DevByteViewModel]. Since we pass
     * [viewModelJob], you can cancel all coroutines launched by [viewModelScope] by calling
     * `viewModelJob.cancel()`
     */
    private val viewModelScope: CoroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Event triggered for network error. This is private to avoid exposing a way to set this value
     * to observers. Public read-only access is provide by our property [eventNetworkError].
     */
    @Suppress("RemoveExplicitTypeArguments") // I like type arguments!
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get read-only access to
     * [_eventNetworkError]. An `Observer` is added to it in the `onCreateView` override of
     * `DevByteFragment` which calls its `onNetworkError` method if it transitions to `true`.
     * It is read by the binding expression for the "app:isNetworkError" attribute of the
     * `ProgressBar` with ID R.id.loading_spinner in the layout file layout/fragment_dev_byte.xml
     * ("app:isNetworkError" is one of two attributes used for the BindingAdapter `hideIfNetworkError`,
     * and the other is "app:playlist" and both are used for that `ProgressBar`). `hideIfNetworkError`
     * sets the visibility of the `View` with the attribute "app:isNetworkError" to GONE if
     * [eventNetworkError] transitions to `true`.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to suppress the display of the error message for a second time. This is private to avoid
     * exposing a way to set this value to observers. Public read-only access is provided by our
     * property [isNetworkErrorShown]. Set to `true` by our [onNetworkErrorShown] method, and set to
     * `false` by our method [refreshDataFromRepository]. The `onNetworkError` method of
     * `DevByteFragment` will toast an error message if it is `true`, then call our method
     * [onNetworkErrorShown] to reset it to `false`. `onNetworkError` is called by an `Observer`
     * of our [eventNetworkError] event when it transitions to `true`.
     */
    @Suppress("RemoveExplicitTypeArguments") // I like type arguments!
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to suppress the display of the error message for a second time. Views should use this to
     * get read-only access to [_isNetworkErrorShown]. An `Observer` added to our [eventNetworkError]
     * property in the `onCreateView` override of `DevByteFragment` calls its `onNetworkError` method
     * when [eventNetworkError] transitions to `true`, and `onNetworkError` will toast an error message
     * when [isNetworkErrorShown] is `false` then call our [onNetworkErrorShown] to set it to `true`
     * to avoid toasting twice if the user rotates the device.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * init{} is called immediately when this AndroidViewModel is created. We just call our
     * `refreshDataFromRepository` method to have it call the `refreshVideos` method of
     * our `VideosRepository` field `videosRepository` to reload our database from the network.
     */
    init {
        refreshDataFromRepository()
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a background thread. We
     * `launch` a lambda using our [CoroutineScope] field [viewModelScope], which, wrapped in a `try`
     * block intended to catch [IOException], calls the `refreshVideos` suspending function of our
     * [VideosRepository] field [videosRepository] and when that function completes sets the `value`
     * of both [_eventNetworkError] and [_isNetworkErrorShown] to `false`. If we catch an [IOException]
     * we check whether the `value` of [playlist] is null or empty and if so we set the `value` of
     * [_eventNetworkError] to `true` (the observer of this event will toast an error message and
     * hide the progress bar).
     */
    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                videosRepository.refreshVideos()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (playlist.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }


    /**
     * Sets the [_isNetworkErrorShown] suppress network error message flag to `true`. Called from
     * the `onNetworkError` method of `DevByteFragment`, which is called from an observer of our
     * `LiveData<Boolean>` property [eventNetworkError] after toasting an error message when
     * [eventNetworkError] changes to `true` state. The [_isNetworkErrorShown] property prevents
     * the `onNetworkError` method of `DevByteFragment` from toasting the error message more than
     * once.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }


    /**
     * Cancel all coroutines when the ViewModel is cleared. First we call our super's implementation
     * of `onCleared`, then we call the `cancel` method of our [CompletableJob] field [viewModelJob]
     * to cancel all the coroutines we may have created.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing DevByteViewModel with [Application] parameter.
     *
     * @param app the [Application] that owns this activity.
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        /**
         * Creates a new instance of the given [Class]. After a sanity check to make sure we are
         * only being used to create a [DevByteViewModel] instance we return a [DevByteViewModel]
         * constructed to use our [app] property as its [Application] property `app`.
         *
         * @param modelClass a [Class] whose instance is requested
         * @param T          The type parameter for the ViewModel.
         * @return a newly created [DevByteViewModel] view model.
         */
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") // Silly warning after the if statement
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
