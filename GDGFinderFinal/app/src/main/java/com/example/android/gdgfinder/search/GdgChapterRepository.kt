package com.example.android.gdgfinder.search
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
 */


import android.location.Location
import com.example.android.gdgfinder.network.GdgApiService
import com.example.android.gdgfinder.network.GdgChapter
import com.example.android.gdgfinder.network.GdgResponse
import com.example.android.gdgfinder.network.LatLong
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * The Repository for our app. There is no offline cache for simplicity and the result of the
 * network is just cached in memory.
 */
class GdgChapterRepository(gdgApiService: GdgApiService) {

    /**
     * A single network request, the results won't change. For this lesson we did not add an offline
     * cache for simplicity and the result will be cached in memory.
     */
    private val request = gdgApiService.getChapters()

    /**
     * An in-progress (or potentially completed) sort, this may be null or cancelled at any time.
     * If this is non-null, calling await will get the result of the last sorting request.
     * This will be cancelled whenever location changes, as the old results are no longer valid.
     */
    private var inProgressSort: Deferred<SortedData>? = null

    /**
     * Set to `true` in our [onLocationChanged] method when the devices location has been found.
     * The `init` block of `GdgListViewModel` launches a coroutine which waits 5 seconds then sets
     * the value of its `MutableLiveData` wrapped [Boolean] property `_showNeedLocation` to the
     * inverse of [isFullyInitialized] which triggers an observer of `showNeedLocation` created
     * in the `onCreateView` override of `GdgListFragment` to pop up a `Snackbar` to tell the user
     * to enable location for the app if it is `true` (ie. [isFullyInitialized] is `false`).
     */
    var isFullyInitialized = false
        private set

    /**
     * Get the chapters list for a specified filter. This will be cancel if a new location is sent
     * before the result is available. This works by first waiting for any previously in-progress
     * sorts, and if a sort has not yet started it will start a new sort (which may happen if
     * location is disabled on the device).
     *
     * @param filter the region key of the `chaptersByRegion` map of the [SortedData] created from
     * our `Deferred<GdgResponse>` field [request] that we wish to filter by.
     * @return the [GdgChapter] list for the specified region [filter].
     */
    suspend fun getChaptersForFilter(filter: String?): List<GdgChapter> {
        val data = sortedData()
        return when(filter) {
            null -> data.chapters
            else -> data.chaptersByRegion.getOrElse(filter) { emptyList() }
        }
    }

    /**
     * Get the filters sorted by distance from the last location.
     *
     * This will cancel if a new location is sent before the result is available.
     *
     * This works by first waiting for any previously in-progress sorts, and if a sort has not yet
     * started it will start a new sort (which may happen if location is disabled on the device)
     */
    suspend fun getFilters(): List<String> = sortedData().filters

    /**
     * Get the computed sorted data after it completes, or start a new sort if none are running.
     *
     * This will always cancel if the location changes while the sort is in progress.
     */
    private suspend fun sortedData(): SortedData = withContext(Dispatchers.Main) {
        inProgressSort?.await() ?: doSortData()
        /**
         * We need to ensure we're on Dispatchers.Main so that this is not running on multiple
         * Dispatchers and we modify the member inProgressSort. Since this was called from
         * viewModelScope, that will always be a simple if check (not expensive), but by specifying
         * the dispatcher we can protect against incorrect usage. If there's currently a sort
         * running (or completed) wait for it to complete and return that value otherwise, start a
         * new sort with no location (the user has likely not given us permission to use location
         * yet)
         */
    }

    /**
     * Call this to force a new sort to start.
     *
     * This will start a new coroutine to perform the sort. Future requests to sorted data can use
     * the deferred in [inProgressSort] to get the result of the last sort without sorting the data
     * again. This guards against multiple sorts being performed on the same data, which is
     * inefficient.
     *
     * This will always cancel if the location changes while the sort is in progress.
     *
     * @return the result of the started sort
     */
    private suspend fun doSortData(location: Location? = null): SortedData {
        /**
         * Since we'll need to launch a new coroutine for the sorting use coroutineScope.
         * coroutineScope will automatically wait for anything started via async {} or await{}
         * in it's block to complete.
         */
        @Suppress("UnnecessaryVariable")
        val result = coroutineScope {
            /**
             * launch a new coroutine to do the sort (so other requests can wait for this sort to
             * complete)
             */
            val deferred = async { SortedData.from(request.await(), location) }
            // cache the Deferred so any future requests can wait for this sort
            inProgressSort = deferred
            // and return the result of this sort
            deferred.await()
        }
        return result
    }

    /**
     * Call when location changes.
     *
     * This will cancel any previous queries, so it's important to re-request the data after
     * calling this function.
     *
     * @param location the location to sort by
     */
    suspend fun onLocationChanged(location: Location) {
        /**
         * We need to ensure we're on Dispatchers.Main so that this is not running on multiple
         * Dispatchers and we modify the member inProgressSort. Since this was called from
         * viewModelScope, that will always be a simple if check (not expensive), but by
         * specifying the dispatcher we can protect against incorrect usage.
         */
        withContext(Dispatchers.Main) {
            isFullyInitialized = true

            // cancel any in progress sorts, their result is not valid anymore.
            inProgressSort?.cancel()

            doSortData(location)
        }
    }

    /**
     * Holds data sorted by the distance from the last location.
     *
     * Note, by convention this class won't sort on the Main thread. This is not a public API and
     * should only be called by [doSortData].
     */
    private class SortedData private constructor(
        val chapters: List<GdgChapter>,
        val filters: List<String>,
        val chaptersByRegion: Map<String, List<GdgChapter>>
    ) {

        companion object {
            /**
             * Sort the data from a [GdgResponse] by the specified location.
             *
             * @param response the response to sort
             * @param location the location to sort by, if null the data will not be sorted.
             */
            suspend fun from(response: GdgResponse, location: Location?): SortedData {
                return withContext(Dispatchers.Default) {
                    // this sorting is too expensive to do on the main thread, so do thread confinement here.
                    val chapters: List<GdgChapter> = response.chapters.sortByDistanceFrom(location)
                    // use distinctBy which will maintain the input order - this will have the effect of making
                    // a filter list sorted by the distance from the current location
                    val filters: List<String> = chapters.map { it.region } .distinctBy { it }
                    // group the chapters by region so that filter queries don't require any work
                    val chaptersByRegion: Map<String, List<GdgChapter>> = chapters.groupBy { it.region }
                    // return the sorted result
                    SortedData(chapters, filters, chaptersByRegion)
                }

            }


            /**
             * Sort a list of [GdgChapter] by their distance from the specified location.
             *
             * @param currentLocation returned list will be sorted by the distance, or unsorted if null
             */
            private fun List<GdgChapter>.sortByDistanceFrom(currentLocation: Location?): List<GdgChapter> {
                currentLocation ?: return this

                return sortedBy { distanceBetween(it.geo, currentLocation)}
            }

            /**
             * Calculate the distance (in meters) between a LatLong and a Location.
             */
            private fun distanceBetween(start: LatLong, currentLocation: Location): Float {
                val results = FloatArray(3)
                Location.distanceBetween(
                    start.lat,
                    start.long,
                    currentLocation.latitude,
                    currentLocation.longitude,
                    results
                )
                return results[0]
            }
        }
    }
}