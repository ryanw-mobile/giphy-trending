package uk.ryanwong.giphytrending

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class GiphyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        } else {
            // TODO: If Firebase Crashlytics is available, replace with a CrashReportingTree here
            plant(Timber.DebugTree())
        }
    }

}