package uk.ryanwong.giphytrending.data.source.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.data.source.local.GiphyDatabase
import uk.ryanwong.giphytrending.data.source.local.RoomDbDataSource
import uk.ryanwong.giphytrending.data.source.local.RoomDbDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RoomDbDataSourceModule {
    @Provides
    @ViewModelScoped
    fun provideRoomDbDataSource(
        giphyDatabase: GiphyDatabase
    ): RoomDbDataSource {
        return RoomDbDataSourceImpl(giphyDatabase = giphyDatabase)
    }
}