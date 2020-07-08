/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.dessertclicker

import android.app.Application
import timber.log.Timber

/**
 * This custom [Application] subclass allows us to initialize the [Timber]
 * library once for the entire app before everything else is set up.
 */
@Suppress("unused")
class ClickerApplication : Application() {
    /**
     * Called when the application is starting, before any activity, service, or receiver objects
     * (excluding content providers) have been created.
     *
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     *
     * If you override this method, be sure to call `super.onCreate()`.
     *
     * Be aware that direct boot may also affect callback order on
     * Android Version N and later devices. Until the user unlocks the
     * device, only direct boot aware components are allowed to run. You
     * should consider that all direct boot unaware components, including
     * such `android.content.ContentProvider`, are disabled until user unlock
     * happens, especially when component callback order matters.
     *
     * First we call our super's implementation of `onCreate`, then we call the [Timber.plant]
     * method to have it add a new [Timber.DebugTree] logging tree for debug builds.
     */
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}
