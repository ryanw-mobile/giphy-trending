package uk.ryanwong.giphytrending.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import uk.ryanwong.giphytrending.BuildConfig
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.source.local.GiphyDatabase
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.GiphyApiService
import uk.ryanwong.giphytrending.ui.GiphyImageItemAdapter
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): GiphyDatabase = Room.databaseBuilder(
        context,
        GiphyDatabase::class.java,
        BuildConfig.DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideApi(): GiphyApi = GiphyApiService.getClient()

    @Provides
    fun provideTrendingRepository() = GiphyRepository()

    @Provides
    fun provideTrendingAdapter(): GiphyImageItemAdapter = GiphyImageItemAdapter().apply {
        setHasStableIds(true)
    }
}