/*
 * Copyright 2024-2026 RW MobiMedia UK Limited
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeUITestTrendingRepository @Inject constructor() : TrendingRepository {
    private val _trendingFlow = MutableStateFlow<List<GifObject>>(emptyList())
    override val trendingFlow: Flow<List<GifObject>> = _trendingFlow

    private var refreshResult: Result<Unit> = Result.success(Unit)

    override suspend fun refreshTrending(limit: Int, rating: Rating): Result<Unit> = refreshResult

    fun setTrendingResultForTest(trendingResult: Result<List<GifObject>>) {
        if (trendingResult.isSuccess) {
            _trendingFlow.value = trendingResult.getOrNull() ?: emptyList()
            this.refreshResult = Result.success(Unit)
        } else {
            this.refreshResult = Result.failure(trendingResult.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}
