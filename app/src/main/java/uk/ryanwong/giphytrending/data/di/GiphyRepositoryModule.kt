package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.GiphyRepositoryImpl
import uk.ryanwong.giphytrending.data.source.local.RoomDbDataSource
import uk.ryanwong.giphytrending.data.source.network.NetworkDataSource
import uk.ryanwong.giphytrending.di.IoDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GiphyRepositoryModule {
    @Singleton
    @Provides
    fun provideGiphyRepository(
        networkDataSource: NetworkDataSource,
        roomDbDataSource: RoomDbDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): GiphyRepository {
        return GiphyRepositoryImpl(
            networkDataSource = networkDataSource,
            roomDbDataSource = roomDbDataSource,
            dispatcher = dispatcher
        )
    }
}
