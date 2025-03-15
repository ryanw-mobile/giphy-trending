/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rwmobi.giphytrending.data.source.local.dao.TrendingDao
import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity

@Database(entities = [TrendingEntity::class], version = 7, exportSchema = false)
@TypeConverters(InstantTypeConverters::class)
abstract class GiphyDatabase : RoomDatabase() {
    abstract fun trendingDao(): TrendingDao
}
