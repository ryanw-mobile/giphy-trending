/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local.interfaces

import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import kotlinx.coroutines.flow.Flow

interface DatabaseDataSource {
    suspend fun insertData(data: TrendingEntity)
    suspend fun insertAllData(data: List<TrendingEntity>)
    fun queryData(): Flow<List<TrendingEntity>>
    suspend fun clear()
    suspend fun markDirty()
    suspend fun deleteDirty()
}
