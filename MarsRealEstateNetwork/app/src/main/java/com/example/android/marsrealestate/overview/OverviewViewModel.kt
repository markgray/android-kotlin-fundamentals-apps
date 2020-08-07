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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    /**
     * The internal [MutableLiveData] wrapped [String] that stores the most recent response. Read
     * only access is provided by [response]. Set by our [getMarsRealEstateProperties] method to
     * "Success" with the number of Mars properties retrieved if the network request succeeded of
     * to "Failure" with the `message` field of the [Exception] thrown if the request failed.
     */
    private val _response = MutableLiveData<String>()

    /**
     * The external immutable LiveData for the response String. Read by a binding expression to set
     * the text of the `TextView` in our layout file layout/fragment_overview.xml when it changes
     * value.
     */
    val response: LiveData<String>
        get() = _response

    /**
     * Create a Coroutine scope using a job to be able to cancel when needed
     */
    private var viewModelJob = Job()

    /**
     * The [CoroutineScope] we use in our [getMarsRealEstateProperties] method to download Mars
     * properties using the `getProperties` method of our retrofit `MarsApiService`. The Coroutine
     * we launch there runs using the Main (UI) dispatcher, then suspends while calling the suspend
     * method `await` on the `Deferred` return value.
     */
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the [_response] immutable [LiveData] to the Mars API failure status or the
     * successful number of Mars properties retrieved. We launch a new coroutine without blocking
     * the current thread using our [CoroutineScope] field [coroutineScope]. The lambda of the
     * coroutine initializes its `Deferred<List<MarsProperty>>` variable `var getPropertiesDeferred`
     * to the Deferred object returned by the `getProperties` method of our [MarsApi] retrofit
     * service. Then in a `try` block intended to catch any [Exception] it initializes its
     * `List<MarsProperty>` variable `var listResult` to the result of calling the `await` method
     * of `getPropertiesDeferred` (waiting for the completion of the download without blocking the
     * thread then resuming when deferred computation is complete). When we resume we set the value
     * of our [MutableLiveData] wrapped [String] field [_response] to "Success" with the number of
     * Mars properties retrieved in `listResult`. If the `try` block catches an exception we set the
     * value of our [MutableLiveData] wrapped [String] field [_response] to "Failure" with the
     * `message` field of the [Exception] thrown.
     */
    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            @Suppress("CanBeVal")
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {
                // Await the completion of our Retrofit request
                @Suppress("CanBeVal")
                var listResult = getPropertiesDeferred.await()
                _response.value = "Success: ${listResult.size} Mars properties retrieved"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
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
