package uk.ryanwong.giphytrending

import android.app.Application
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
    }
}