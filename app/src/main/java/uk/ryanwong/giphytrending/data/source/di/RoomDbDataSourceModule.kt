package uk.ryanwong.giphytrending.data.source.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.data.source.local.GiphyDatabase
import uk.ryanwong.giphytrending.data.source.local.RoomDbDataSource
import uk.ryanwong.giphytrending.data.source.local.RoomDbDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbDataSourceModule {
    @Provides
    @Singleton
    fun provideRoomDbDataSource(
        giphyDatabase: GiphyDatabase
    ): RoomDbDataSource {
        return RoomDbDataSourceImpl(giphyDatabase = giphyDatabase)
    }
}