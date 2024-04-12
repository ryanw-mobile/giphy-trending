package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.repository.GiphyRepositoryImpl
import com.rwmobi.giphytrending.data.source.local.RoomDbDataSource
import com.rwmobi.giphytrending.data.source.network.NetworkDataSource
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object GiphyRepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideGiphyRepository(
        networkDataSource: NetworkDataSource,
        roomDbDataSource: RoomDbDataSource,
        @GiphyApiKey giphyApiKey: String,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): GiphyRepository {
        return GiphyRepositoryImpl(
            networkDataSource = networkDataSource,
            roomDbDataSource = roomDbDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }
}
