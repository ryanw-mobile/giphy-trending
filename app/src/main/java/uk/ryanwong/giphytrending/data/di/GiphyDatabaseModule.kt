package uk.ryanwong.giphytrending.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.BuildConfig
import uk.ryanwong.giphytrending.data.source.local.GiphyDatabase
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object GiphyDatabaseModule {
    @ViewModelScoped
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): GiphyDatabase {
        return Room.databaseBuilder(
            applicationContext,
            GiphyDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}