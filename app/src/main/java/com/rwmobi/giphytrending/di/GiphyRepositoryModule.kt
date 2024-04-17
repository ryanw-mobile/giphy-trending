/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.repository.GiphyRepositoryImpl
import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
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
        databaseDataSource: DatabaseDataSource,
        @GiphyApiKey giphyApiKey: String,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): GiphyRepository {
        return GiphyRepositoryImpl(
            networkDataSource = networkDataSource,
            databaseDataSource = databaseDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }
}
