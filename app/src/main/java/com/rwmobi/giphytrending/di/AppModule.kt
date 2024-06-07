/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rwmobi.giphytrending.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"
val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class GiphyApiKey

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(
            context = SupervisorJob() + Dispatchers.Default + CoroutineExceptionHandler { _, throwable ->
                // Handle uncaught exceptions from this scope.
                Timber.tag("CoroutineScope").e("Unhandled exception: $throwable")
            },
        )
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.userDataStore
    }

    @GiphyApiKey
    @Singleton
    @Provides
    fun provideGiphyApiKey() = BuildConfig.GIPHY_API_KEY
}
