/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity

class FakeDatabaseDataSource : DatabaseDataSource {
    var apiError: Throwable? = null

    override suspend fun insertData(data: TrendingEntity) {
        apiError?.run { throw this }
        mockQueryDataResponse = listOf(data)
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        apiError?.run { throw this }
        mockQueryDataResponse = data
    }

    var mockQueryDataResponse: List<TrendingEntity>? = null
    override suspend fun queryData(): List<TrendingEntity> {
        apiError?.run { throw this }
        return mockQueryDataResponse ?: emptyList()
    }

    override suspend fun clear() {
        apiError?.run { throw this }
        mockQueryDataResponse = emptyList()
    }

    override suspend fun markDirty() {
        apiError?.run { throw this }
    }

    override suspend fun deleteDirty() {
        apiError?.run { throw this }
    }
}
