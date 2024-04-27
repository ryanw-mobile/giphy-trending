/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.ui.test.FakeUserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [UserPreferencesRepositoryModule::class],
)
object FakeUserPreferencesRepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideFakeUserPreferencesRepository(): UserPreferencesRepository {
        return FakeUserPreferencesRepository()
    }
}
