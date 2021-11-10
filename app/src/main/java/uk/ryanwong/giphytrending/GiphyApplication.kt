package uk.ryanwong.giphytrending

import android.app.Application
import uk.ryanwong.giphytrending.data.source.local.TrendingDatabase

class GiphyApplication : Application() {
    companion object {
        lateinit var instance: GiphyApplication
        lateinit var database: TrendingDatabase
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        database = TrendingDatabase.invoke(this)
    }
}