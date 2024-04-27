/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import com.rwmobi.giphytrending.ui.test.FakeTrendingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [TrendingRepositoryModule::class],
)
object FakeTrendingRepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideFakeTrendingRepository(): TrendingRepository {
        return FakeTrendingRepository()
    }
}
