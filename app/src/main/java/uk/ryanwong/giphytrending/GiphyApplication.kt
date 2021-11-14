package uk.ryanwong.giphytrending

import android.app.Application
import timber.log.Timber
import timber.log.Timber.Forest.plant
import uk.ryanwong.giphytrending.di.AppComponent
import uk.ryanwong.giphytrending.di.AppModule
import uk.ryanwong.giphytrending.di.DaggerAppComponent

class GiphyApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        } else {
            // TODO: If Firebase Crashlytics is available, replace with a CrashReportingTree here
            plant(Timber.DebugTree())
        }
    }

}