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

package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import timber.log.Timber

/**
 * A `ListenableWorker` implementation that provides interop with Kotlin Coroutines. We override
 * the [doWork] function to do our suspending work. This is used in the `setupRecurringWork` method
 * of `DevByteApplication` where it schedules it to run on a daily basis using `WorkManager`.
 *
 * @param appContext this [Context] parameter will be supplied by the `WorkManager` when we are run.
 * @param params this instance of [WorkerParameters] comes from the `WorkManager` when we are run.
 */
class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params) {

    companion object {
        /**
         * The unique name for this operation, used in the call to `enqueueUniquePeriodicWork`
         * in the `setupRecurringWork` method of `DevByteApplication`
         */
        const val WORK_NAME = "com.example.android.devbyteviewer.work.RefreshDataWorker"
    }

    /**
     * A suspending method to do our work. This function runs on the coroutine context specified
     * by the [coroutineContext] we are run on, which is by default [Dispatchers.Default].
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [ListenableWorker.Result]. After this time has expired, the worker will be signalled to
     * stop.
     *
     * We initialize our [VideosDatabase] variable `val database` with a handle to the singleton
     * instance of our database. We then initialize our [VideosRepository] variable `val repository`
     * with an instance constructed to use `database` as its [VideosDatabase]. Then in a `try` block
     * intended to catch [HttpException] and return a [ListenableWorker.Result.retry] as our
     * [ListenableWorker.Result] if such an exception occurs, we call the suspend function
     * `refreshVideos` of `repository` in order to have it refresh the videos stored in the offline
     * cache from the network. If there is no exception we return [ListenableWorker.Result.success]
     * as our [ListenableWorker.Result].
     *
     * @return The [ListenableWorker.Result] of the result of the background work, one of
     * [ListenableWorker.Result.success], [ListenableWorker.Result.retry], or
     * [ListenableWorker.Result.failure] Note that dependent work will not execute if you return
     * [ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        val database: VideosDatabase = getDatabase(applicationContext)
        val repository = VideosRepository(database)

        try {
            repository.refreshVideos( )
            Timber.d("WorkManager: Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }
}