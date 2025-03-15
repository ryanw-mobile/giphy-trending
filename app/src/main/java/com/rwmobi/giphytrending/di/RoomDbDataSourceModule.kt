/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import com.rwmobi.giphytrending.data.source.local.GiphyDatabase
import com.rwmobi.giphytrending.data.source.local.RoomDatabaseDataSource
import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RoomDbDataSourceModule {
    @ViewModelScoped
    @Provides
    fun provideRoomDbDataSource(
        giphyDatabase: GiphyDatabase,
    ): DatabaseDataSource {
        return RoomDatabaseDataSource(giphyDatabase = giphyDatabase)
    }
}
