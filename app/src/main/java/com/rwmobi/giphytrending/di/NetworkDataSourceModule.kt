/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.source.network.GiphyApi
import com.rwmobi.giphytrending.data.source.network.RetrofitNetworkDataSource
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataSourceModule {
    @Singleton
    @Provides
    fun provideNetworkDataSource(
        giphyApiService: GiphyApi,
    ): NetworkDataSource {
        return RetrofitNetworkDataSource(giphyApiService = giphyApiService)
    }
}
