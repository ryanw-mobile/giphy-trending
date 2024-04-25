package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.TrendingRepository

class FakeTrendingRepository : TrendingRepository {
    private var trendingResult: Result<List<GiphyImageItem>>? = null

    override suspend fun fetchCachedTrending(): Result<List<GiphyImageItem>> {
        return trendingResult ?: Result.success(emptyList())
    }

    override suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GiphyImageItem>> {
        return trendingResult ?: Result.success(emptyList())
    }

    fun setTrendingResultForTest(trendingResult: Result<List<GiphyImageItem>>?) {
        this.trendingResult = trendingResult
    }
}
