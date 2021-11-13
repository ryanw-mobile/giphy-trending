package uk.ryanwong.giphytrending

import android.app.Application
import timber.log.Timber
import timber.log.Timber.Forest.plant
import uk.ryanwong.giphytrending.data.source.local.GiphyDatabase

class GiphyApplication : Application() {
    companion object {
        lateinit var instance: GiphyApplication
        lateinit var database: GiphyDatabase
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        database = GiphyDatabase.invoke(this)

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        } else {
            // TODO: If Firebase Crashlytics is available, replace with a CrashReportingTree here
            plant(Timber.DebugTree())
        }
    }
}