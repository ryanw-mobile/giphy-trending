package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.GiphyRepository

class FakeGiphyRepository : GiphyRepository {
    var fakeTrendingResult: Result<List<GiphyImageItem>>? = null
    override suspend fun fetchCachedTrending(): Result<List<GiphyImageItem>> {
        return fakeTrendingResult ?: Result.success(emptyList())
    }

    override suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GiphyImageItem>> {
        return fakeTrendingResult ?: Result.success(emptyList())
    }
}
