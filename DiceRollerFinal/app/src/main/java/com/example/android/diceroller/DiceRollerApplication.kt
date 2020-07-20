package com.example.android.diceroller

import android.app.Application
import timber.log.Timber

/**
 * This override of [Application] allows us to `plant` a [Timber] tree for debug  builds.
 * Automatically infers the tag from the calling class.
 */
@Suppress("unused") // It really is used in AndroidManifest.xml
class DiceRollerApplication : Application() {

    /**
     * Called when the application is starting, before any activity, service, or receiver objects
     * (excluding content providers) have been created. First we call our super's implementation of
     * `onCreate`, then we call the [Timber.plant] to add a [Timber] tree for debug  builds.
     */
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}