/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSourceB
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [NetworkDataSourceModule::class],
)
object FakeNetworkDataSourceModule {
    @ViewModelScoped
    @Provides
    fun provideNetworkDataSource(): NetworkDataSource {
        return FakeNetworkDataSourceB()
    }
}
