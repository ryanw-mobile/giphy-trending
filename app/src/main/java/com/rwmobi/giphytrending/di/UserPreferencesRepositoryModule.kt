/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.repository.UserPreferencesRepositoryImpl
import com.rwmobi.giphytrending.data.source.preferences.interfaces.PreferencesDataStoreWrapper
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesRepositoryModule {
    @Singleton
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
