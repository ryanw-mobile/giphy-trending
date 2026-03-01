/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTrendingRepository : TrendingRepository {

    private val _trendingFlow = MutableStateFlow<List<GifObject>>(emptyList())
    override val trendingFlow: Flow<List<GifObject>> = _trendingFlow

    private var refreshResult = Result.success(Unit)

    fun setTrendingResultForTest(result: Result<List<GifObject>>) {
        result.onSuccess {
            _trendingFlow.value = it
        }.onFailure {
            refreshResult = Result.failure(it)
        }
    }

    override suspend fun refreshTrending(limit: Int, rating: Rating): Result<Unit> = refreshResult
}
