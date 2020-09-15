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
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
 * @param database the [SleepDatabaseDao] to use to access the database
 * @param application the [Application] to use to access resources
 */
@Suppress("MemberVisibilityCanBePrivate")
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application
) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] that keeps track of all coroutines started by this ViewModel. Because we
     * pass it [viewModelJob], any coroutine started in this scope can be cancelled by calling
     * `viewModelJob.cancel()`. By default, all coroutines started in [uiScope] will launch in
     * [Dispatchers.Main] which is the main thread on Android. This is a sensible default because
     * most coroutines started by a `ViewModel` update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * The [LiveData] wrapped list of all of the [SleepNight] entries read from the database.
     */
    private val nights = database.getAllNights()

    /**
     * Converted nights to Spanned for displaying. We use the [Transformations.map] method to
     * convert our [LiveData] wrapped list of [SleepNight]'s field [nights] into a [LiveData]
     * wrapped [Spanned] by applying our [formatNights] method to the value in [nights].
     */
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    /**
     * The latest [SleepNight] read back from the database if a sleep recording is in progress (the
     * `endTimeMilli` and `startTimeMilli` fields are equal), or `null` if we are not recording a
     * sleep quality (the last [SleepNight] entered that we read by calling the `getTonight` method
     * of [database] had different `endTimeMilli` and `startTimeMilli` fields).
     */
    private var tonight = MutableLiveData<SleepNight?>()

    init {
        initializeTonight()
    }

    /**
     * Called by our `init` block to initialize our [tonight] field to the last entry added to the
     * database iff it represents a [SleepNight] in progress (`endTimeMilli` == `startTimeMilli`),
     * or to `null` if they are different (the last entry is a completed [SleepNight]). We just
     * launch a coroutine on the [uiScope] `CoroutineScope` which sets the value of [tonight] to
     * the value returned by our suspend function [getTonightFromDatabase].
     */
    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    /**
     * Handling the case of the stopped app or forgotten recording, when the start and end times
     * will be the same. If the start time and end time are not the same, then we do not have an
     * unfinished recording so we return `null`. We call a suspending block with the coroutine
     * context of [Dispatchers.IO], suspending until it completes. The suspending block lambda sets
     * the [SleepNight] variable `var night` to the [SleepNight] returned by the `getTonight` method
     * of [database]. If the `endTimeMilli` field of `night` is not equal to its `startTimeMilli`
     * field we set `night` to `null`. When the lambda completes we return its `night` variable to
     * the caller. Called by our [initializeTonight] method, and by our [onStartTracking] method
     * which is called by a binding expression for the "android:onClick" attribute of the `Button`
     * with ID R.id.start_button in the file layout/fragment_sleep_tracker.xml which is the layout
     * file for `SleepTrackerFragment`.
     *
     * @return the last [SleepNight] added to the database if its `endTimeMilli` and `startTimeMilli`
     * are the same (a sleep recording in progress) or `null` if they differ.
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

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

