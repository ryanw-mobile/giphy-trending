/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import javax.inject.Inject

class RoomDbDataSourceImpl @Inject constructor(
    private val giphyDatabase: GiphyDatabase,
) : RoomDbDataSource {
    override suspend fun insertData(data: TrendingEntity) {
        return giphyDatabase.trendingDao().insertData(data = data)
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        return giphyDatabase.trendingDao().insertAllData(data = data)
    }

    override suspend fun queryData(): List<TrendingEntity> {
        return giphyDatabase.trendingDao().queryData()
    }

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
