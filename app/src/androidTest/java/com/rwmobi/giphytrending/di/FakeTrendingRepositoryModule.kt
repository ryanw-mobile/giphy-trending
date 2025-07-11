/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.repository.FakeUITestTrendingRepository
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TrendingRepositoryModule::class],
)
object FakeTrendingRepositoryModule {
    @Singleton
    @Provides
    fun provideTrendingRepository(): TrendingRepository = FakeUITestTrendingRepository()
}
