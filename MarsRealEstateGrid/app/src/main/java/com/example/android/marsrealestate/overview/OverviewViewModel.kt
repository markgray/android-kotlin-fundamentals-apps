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

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Gets Mars real estate property information from the Mars API Retrofit service and updates the
     * [MarsProperty] [List] [LiveData]. The Retrofit service returns a coroutine Deferred, which we
     * await to get the result of the transaction.
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
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
