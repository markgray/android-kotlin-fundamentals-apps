/*
 * Copyright 2019, The Android Open Source Project
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
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The possible status states of the most recent request. [LOADING] is used after our method
 * `getMarsRealEstateProperties` calls `Retrofit` method `getProperties`, [ERROR] is used if
 * an error occurs while waiting for the result, and [DONE] is used if the download completes
 * successfully.
 */
enum class MarsApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    /**
     * The internal MutableLiveData that stores the status of the most recent request, [status] is
     * the public read-only access to this value. Set to [MarsApiStatus.LOADING] when our method
     * [getMarsRealEstateProperties] calls `Retrofit` method `getProperties` to begin the download
     * of the list of [MarsProperty], set to [MarsApiStatus.ERROR] is an [Exception] is thrown, and
     * to [MarsApiStatus.DONE] if the download completes successfully.
     */
    private val _status = MutableLiveData<MarsApiStatus>()

    /**
     * The external immutable LiveData for the request status. The `bindStatus` binding adapter for
     * the "app:marsApiStatus" attribute accesses this property and uses the value to control the
     * visibility and image displayed by any `ImageView` which uses the attribute "app:marsApiStatus"
     * with the [status] property of this [OverviewViewModel] in a binding expression. The `ImageView`
     * with resource ID `R.id.status_image` in the layout file layout/fragment_overview.xml does so.
     */
    val status: LiveData<MarsApiStatus>
        get() = _status

    /**
     * The list of [MarsProperty] objects downloaded by `Retrofit` from the internet. Internally, we
     * use a MutableLiveData, because we will be updating the List of [MarsProperty] with new values.
     */
    private val _properties = MutableLiveData<List<MarsProperty>>()

    /**
     * The external LiveData interface to the [_properties] property. Immutable, so only this class
     * can modify. A binding expression for the "app:listData" attribute accesses this property when
     * the attribute is used for a `RecyclerView` as the `RecyclerView` with ID `R.id.photos_grid`
     * in the layout file layout/fragment_overview.xml does. "app:listData" is handled by the
     * binding adapter `bindRecyclerView` which retrieves the adapter of the `RecyclerView` and
     * calls its `submitList` method to submit the [properties] list to be diffed, and displayed.
     */
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    /**
     * [Job] to use to Create a Coroutine scope that we can cancel when needed by cancelling the job
     */
    private var viewModelJob = Job()

    /**
     * [CoroutineScope] to run Coroutine using the Main (UI) dispatcher with [viewModelJob] as its
     * Job so that the Coroutine can be cancelled by cancelling [viewModelJob].
     */
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Gets Mars real estate property information from the Mars API Retrofit service and updates the
     * [LiveData] wrapped list of [MarsProperty]. The Retrofit service returns a coroutine Deferred,
     * which we `await` to get the result of the transaction. We launch a new coroutine without
     * blocking the current thread on our [CoroutineScope] field [coroutineScope]. The lambda of the
     * coroutine sets the value of our [MutableLiveData] wrapped [MarsApiStatus] property [_status]
     * to [MarsApiStatus.LOADING] (a binding expression observing [status] for the "app:marsApiStatus"
     * attribute will then have the BindingAdapter `bindStatus` cause the display of the drawable
     * R.drawable.loading_animation in the UI). We initialize our [Deferred] wrapped [List] of
     * [MarsProperty] variable `var getPropertiesDeferred` to the value returned by the `getProperties`
     * method of [MarsApi.retrofitService]. Then wrapped in a `try` block intended to catch any
     * [Exception] we again set value of our [MutableLiveData] wrapped [MarsApiStatus] property
     * [_status] to [MarsApiStatus.LOADING] (probably redundant) then we set our [List] of
     * [MarsProperty] variable `val listResult` to the result of calling the `await` method of
     * `getPropertiesDeferred` (suspending while a Retrofit thread completes the download). When
     * the coroutine resumes we set our [MutableLiveData] wrapped [MarsApiStatus] property [_status]
     * to [MarsApiStatus.DONE] (the `bindStatus` binding adapter for the "app:marsApiStatus"
     * attribute will then cause the visiblity of the `ImageView` with that attribute to `GONE`).
     * We then set the value of our `MutableLiveData<List<MarsProperty>>` property [_properties]
     * to `listResult` (a binding expression observing [properties] for the "app:listData" attribute
     * will then have the BindingAdapter `bindRecyclerView` submit the [List] to the `RecyclerView`).
     *
     * If we catch an [Exception] we set the value of our [MutableLiveData] wrapped [MarsApiStatus]
     * property [_status] to [MarsApiStatus.ERROR] (a binding expression observing [status] for the
     * "app:marsApiStatus" attribute will then have the BindingAdapter `bindStatus` cause the display
     * of the drawable R.drawable.ic_connection_error in the UI), and we set the value of or
     * MutableLiveData<List<MarsProperty>>` property [_properties] to an empty [ArrayList].
     */
    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            _status.value = MarsApiStatus.LOADING
            // this will run on a thread managed by Retrofit
            @Suppress("CanBeVal")
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {
                _status.value = MarsApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val listResult = getPropertiesDeferred.await()
                _status.value = MarsApiStatus.DONE
                _properties.value = listResult
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    /**
     * When the [ViewModel] is finished, we call our super's implementation of `onCleared` then we
     * cancel our coroutine [viewModelJob], which tells the Retrofit service to stop any downloads
     * in progress.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
