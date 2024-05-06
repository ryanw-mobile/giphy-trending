package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.TrendingRepository

class FakeTrendingRepository : TrendingRepository {
    private var trendingResult: Result<List<GifObject>>? = null

    override suspend fun fetchCachedTrending(): Result<List<GifObject>> {
        return trendingResult ?: Result.success(emptyList())
    }

    override suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GifObject>> {
        return trendingResult ?: Result.success(emptyList())
    }

    fun setTrendingResultForTest(trendingResult: Result<List<GifObject>>?) {
        this.trendingResult = trendingResult
    }
}
