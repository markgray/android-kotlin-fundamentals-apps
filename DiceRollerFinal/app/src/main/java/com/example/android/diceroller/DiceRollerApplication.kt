package com.example.android.diceroller

import android.app.Application
import timber.log.Timber

/**
 * This override of [Application] allows us to `plant` a [Timber] tree for debug  builds.
 * Automatically infers the tag from the calling class.
 */
@Suppress("unused")
class DiceRollerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}