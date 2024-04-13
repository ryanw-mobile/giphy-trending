/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.repository.UserPreferencesRepositoryImpl
import com.rwmobi.giphytrending.data.source.preferences.PreferencesDataStoreWrapper
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object UserPreferencesRepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideUserPreferencesRepository(
        preferencesDataStoreWrapper: PreferencesDataStoreWrapper,
        externalCoroutineScope: CoroutineScope,
        @DispatcherModule.MainDispatcher dispatcher: CoroutineDispatcher,
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            preferencesDataStoreWrapper = preferencesDataStoreWrapper,
            externalCoroutineScope = externalCoroutineScope,
            dispatcher = dispatcher,
        )
    }
}
