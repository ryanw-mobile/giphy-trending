/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import android.content.Context
import androidx.room.Room
import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.data.source.local.GiphyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object GiphyDatabaseModule {
    @ViewModelScoped
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): GiphyDatabase {
        return Room.databaseBuilder(
            applicationContext,
            GiphyDatabase::class.java,
            BuildConfig.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }
}
