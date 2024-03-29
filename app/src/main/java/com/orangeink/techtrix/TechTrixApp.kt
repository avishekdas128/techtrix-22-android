package com.orangeink.techtrix

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class TechTrixApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}