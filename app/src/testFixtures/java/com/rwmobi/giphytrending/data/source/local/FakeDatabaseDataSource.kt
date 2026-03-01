/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeDatabaseDataSource : DatabaseDataSource {

    var queryDataResponse = MutableStateFlow<List<TrendingEntity>>(emptyList())
    var queryDataError: Exception? = null
    var apiError: Exception? = null

    override suspend fun insertData(data: TrendingEntity) {
        // no-op
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        apiError?.let { throw it }
        queryDataResponse.value = data
    }

    override fun queryData(): Flow<List<TrendingEntity>> {
        queryDataError?.let { throw it }
        return queryDataResponse
    }

    override suspend fun clear() {
        // no-op
    }

    override suspend fun markDirty() {
        apiError?.let { throw it }
    }

    override suspend fun deleteDirty() {
        apiError?.let { throw it }
    }
}
