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

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for `SleepTrackerFragment`.
 *
 * @param dataSource the [SleepDatabaseDao] to use to access the database
 * @param application the [Application] to use to access resources
 */
class SleepTrackerViewModel(
        dataSource: SleepDatabaseDao,
        application: Application
) : ViewModel() {

    /**
     * Hold a reference to SleepDatabase via SleepDatabaseDao.
     */
    val database = dataSource

    /** Coroutine variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] that keeps track of all coroutines started by this ViewModel. Because we
     * pass it [viewModelJob], any coroutine started in this scope can be cancelled by calling
     * `viewModelJob.cancel()`. By default, all coroutines started in [uiScope] will launch in
     * [Dispatchers.Main] which is the main thread on Android. This is a sensible default because
     * most coroutines started by a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * The latest [SleepNight] read back from the database if a sleep recording is in progress (the
     * `endTimeMilli` and `startTimeMilli` fields are equal), or `null` if we are not recording a
     * sleep quality (the last [SleepNight] entered that we read by calling the `getTonight` method
     * of [database] had different `endTimeMilli` and `startTimeMilli` fields). When non-null we
     * update it in our [onStop] method when the STOP button is clicked.
     */
    private var tonight = MutableLiveData<SleepNight?>()

    /**
     * The [LiveData] wrapped list of all of the [SleepNight] entries read from the database.
     */
    val nights = database.getAllNights()

    /**
     * Converted nights to Spanned for displaying (used before the RecyclerView was added).
     */
    @Suppress("unused")
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    /**
     * If tonight has not been set, then the START button should be visible.
     */
    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }

    /**
     * If tonight has been set, then the STOP button should be visible.
     */
    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }

    /**
     * If there are any nights in the database, show the CLEAR button.
     */
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    /**
     * Request a Snackbar by setting this value to true. This is private because we don't want to
     * expose setting this value to the Fragment, publicly available read-only access is provided
     * by [showSnackBarEvent]. This is set to `true` by our [onClear] method and set to `null` by
     * our [doneShowingSnackbar] method. [onClear] is called by a binding expression for the
     * "android:onClick" attribute of the "Clear" button, and [doneShowingSnackbar] is called by
     * the `Observer` added to [showSnackBarEvent] after it shows the Snackbar.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    /**
     * If this is true, immediately `show()` a Snackbar and call [doneShowingSnackbar] to reset to
     * `null`. An `Observer` is added to it in the `onCreateView` override of `SleepTrackerFragment`
     * which shows a Snackbar informing the user "All your data is gone forever"
     */
    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    /**
     * Variable that tells the Fragment to navigate to `SleepQualityFragment` using the specified
     * [SleepNight] as the safe args for the fragment. This is private because we don't want to
     * expose setting this value to the Fragment. Set to the value of our [tonight] if it is not
     * `null` by our [onStop] method which is called by a binding expression for the "android:onClick"
     * attribute of the "Stop" button. Set to `null` by our [doneNavigating] method which is called
     * after navigating to the `SleepQualityFragment` to prevent repeated navigating.
     */
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

    /**
     * If this is non-null, immediately navigate to `SleepQualityFragment` and call [doneNavigating].
     * An `Observer` is added to it in the `onCreateView` override of `SleepTrackerFragment` which
     * navigates to `SleepQualityFragment` using the `nightId` primary key of the [SleepNight] as
     * the safe args to pass.
     */
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    /**
     * Call this immediately after calling `show()` on a Snackbar. It will clear the Snackbar request,
     * so if the user rotates their phone it won't show a duplicate Snackbar.
     */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    /**
     * Call this immediately after navigating to `SleepQualityFragment`. It will clear the navigation
     * request, so if the user rotates their phone it won't navigate twice.
     */
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    /**
     * Variable that tells the Fragment to navigate to `SleepDetailFragment` using the specified
     * [Long] as the safe args for the fragment (the `nightId` of the [SleepNight] whose item view
     * was clicked). This is private because we don't want to expose setting this value to the
     * Fragment. Public read-only access is provided by [navigateToSleepDetail]. Set to the argument
     * passed to our [onSleepNightClicked] method by the `SleepNightListener` which each item view
     * binding (for layout file layout/list_item_sleep_night.xml) uses as its `clickListener` variable
     * which is in turn invoked by a binding expression for the "android:onClick" attribute of the
     * containing `ConstraintLayout` with its [SleepNight] variable `sleep` (the `SleepNightListener`
     * passes the `nightId` field of the [SleepNight] it is called with). Set to `null` by our
     * [onSleepDetailNavigated] method which is called by the `Observer` of [navigateToSleepDetail]
     * after it navigates to `SleepDetailFragment`.
     */
    private val _navigateToSleepDetail = MutableLiveData<Long>()

    /**
     * Public read-only access to our [_navigateToSleepDetail] field. If this is non-null, immediately
     * navigate to `SleepDetailFragment` and call [onSleepDetailNavigated]. An `Observer` is added to
     * it in the `onCreateView` override of `SleepTrackerFragment` which, when it transitions to
     * non-null, navigates to `SleepDetailFragment` passing the new value as the safe args `nightId`
     * primary key of the [SleepNight] to display.
     */
    val navigateToSleepDetail
        get() = _navigateToSleepDetail

    fun onSleepNightClicked(id: Long) {
        _navigateToSleepDetail.value = id
    }

    fun onSleepDetailNavigated() {
        _navigateToSleepDetail.value = null
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */
    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    /**
     * Executes when the START button is clicked.
     */
    fun onStart() {
        uiScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newNight = SleepNight()

            insert(newNight)

            tonight.value = getTonightFromDatabase()
        }
    }

    /**
     * Executes when the STOP button is clicked.
     */
    fun onStop() {
        uiScope.launch {
            // In Kotlin, the return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch().
            val oldNight = tonight.value ?: return@launch

            // Update the night in the database to add the end time.
            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)

            // Set state to navigate to the SleepQualityFragment.
            _navigateToSleepQuality.value = oldNight
        }
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun onClear() {
        uiScope.launch {
            // Clear the database table.
            clear()

            // And clear tonight since it's no longer in the database
            tonight.value = null

            // Show a snackbar message, because it's friendly.
            _showSnackbarEvent.value = true
        }
    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}