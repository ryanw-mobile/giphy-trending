/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import javax.inject.Inject

class RoomDatabaseDataSource @Inject constructor(
    private val giphyDatabase: GiphyDatabase,
) : DatabaseDataSource {
    override suspend fun insertData(data: TrendingEntity) = giphyDatabase.trendingDao().insertData(data = data)

    override suspend fun insertAllData(data: List<TrendingEntity>) = giphyDatabase.trendingDao().insertAllData(data = data)

    override suspend fun queryData(): List<TrendingEntity> = giphyDatabase.trendingDao().queryData()

    override suspend fun clear() {
        giphyDatabase.trendingDao().clear()
    }

    override suspend fun markDirty() {
        giphyDatabase.trendingDao().markDirty()
    }

    override suspend fun deleteDirty() {
        giphyDatabase.trendingDao().deleteDirty()
    }
}
