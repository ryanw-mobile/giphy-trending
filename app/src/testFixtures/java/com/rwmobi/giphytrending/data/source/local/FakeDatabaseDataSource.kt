/*
 * Copyright 2024-2026 RW MobiMedia UK Limited
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity

class FakeDatabaseDataSource : DatabaseDataSource {
    var apiError: Throwable? = null
    var queryDataResponse: List<TrendingEntity>? = null

    override suspend fun insertData(data: TrendingEntity) {
        apiError?.run { throw this }
        queryDataResponse = listOf(data)
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        apiError?.run { throw this }
        queryDataResponse = data
    }

    override suspend fun queryData(): List<TrendingEntity> {
        apiError?.run { throw this }
        return queryDataResponse ?: emptyList()
    }

    override suspend fun clear() {
        apiError?.run { throw this }
        queryDataResponse = emptyList()
    }

    override suspend fun markDirty() {
        apiError?.run { throw this }
    }

    override suspend fun deleteDirty() {
        apiError?.run { throw this }
    }
}
