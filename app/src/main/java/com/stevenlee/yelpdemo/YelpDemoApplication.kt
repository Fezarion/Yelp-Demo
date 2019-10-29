package com.stevenlee.yelpdemo

import android.app.Application
import timber.log.Timber

class YelpDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // Set Timber on DEBUG mode.
            Timber.plant(Timber.DebugTree())
        }
    }
}