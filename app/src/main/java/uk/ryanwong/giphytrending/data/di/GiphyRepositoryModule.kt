package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.GiphyRepositoryImpl
import uk.ryanwong.giphytrending.data.source.local.RoomDbDataSource
import uk.ryanwong.giphytrending.data.source.network.NetworkDataSource
import uk.ryanwong.giphytrending.di.IoDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object GiphyRepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideGiphyRepository(
        networkDataSource: NetworkDataSource,
        roomDbDataSource: RoomDbDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): GiphyRepository {
        return GiphyRepositoryImpl(
            networkDataSource = networkDataSource,
            roomDbDataSource = roomDbDataSource,
            dispatcher = dispatcher,
        )
    }
}
