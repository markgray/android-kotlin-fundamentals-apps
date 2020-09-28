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

package com.example.android.devbyteviewer

import android.app.Application
import android.os.Build
import androidx.work.*

import com.example.android.devbyteviewer.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */
@Suppress("unused") // It is actually used in our AndroidManifest.xml file.
class DevByteApplication : Application() {

    /**
     * [CoroutineScope] we use to set up our [WorkManager] periodic refresh of our database using a
     * background thread from the shared pool of threads of [Dispatchers.Default]
     */
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * [onCreate] is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     *
     * First we call our super's implementation of `onCreate`, then we call our [delayedInit] method
     * to add a new logging tree for debug builds to [Timber], and to call our [setupRecurringWork]
     * method to set up our [WorkManager] periodic refresh of our database from the network.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    /**
     * Launches a lambda on our [CoroutineScope] field [applicationScope] which calls the [Timber.plant]
     * method to add a new logging tree for debug builds to [Timber], and calls our [setupRecurringWork]
     * to set up our [WorkManager] periodic refresh of our database from the network.
     */
    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
            setupRecurringWork()
        }
    }

    /**
     * Setup WorkManager background job to 'fetch' new network data daily. We initialize our
     * [Constraints] variable `val constraints` to the results of using a [Constraints.Builder]
     * instance to set the required network type to [NetworkType.UNMETERED], require the battery
     * to be charging, require that the battery not be low, if the build version is greater than
     * "M" require that the device is idle, and then building that [Constraints.Builder].
     *
     * We initialize our [PeriodicWorkRequest] variable `val repeatingRequest` by using a
     * [PeriodicWorkRequestBuilder] for our [CoroutineWorker] class [RefreshDataWorker] with a
     * repeat interval of 1 days, adding `constraints` as the constraints for the work then
     * building that [PeriodicWorkRequest.Builder].
     *
     * We then log that "WorkManager: Periodic Work request for sync is scheduled". We then retrieve
     * the default singleton instance of [WorkManager], and use its `enqueueUniquePeriodicWork`
     * method to enqueue a [PeriodicWorkRequest] with the unique name [RefreshDataWorker.WORK_NAME],
     * an existing periodic work policy of [ExistingPeriodicWorkPolicy.KEEP] (if there is existing
     * pending (uncompleted) work with the same unique name, do nothing. Otherwise, insert the new
     * work), and `repeatingRequest` as the [PeriodicWorkRequest] to enqueue.
     *
     */
    private fun setupRecurringWork() {

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .setRequiresBatteryNotLow(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }
                .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
                1,
                TimeUnit.DAYS
        ).setConstraints(constraints).build()

        Timber.d("WorkManager: Periodic Work request for sync is scheduled")
        WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }
}
