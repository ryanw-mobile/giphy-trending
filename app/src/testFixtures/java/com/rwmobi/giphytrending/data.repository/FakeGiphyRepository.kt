package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.repository.GiphyRepository

class FakeGiphyRepository : GiphyRepository {
    var mockFetchCachedTrendingResult: Result<List<GiphyImageItem>>? = null
    override suspend fun fetchCachedTrending(): Result<List<GiphyImageItem>> {
        return mockFetchCachedTrendingResult ?: Result.success(emptyList())
    }

    var mockReloadTrendingResult: Result<List<GiphyImageItem>>? = null
    override suspend fun reloadTrending(apiMaxEntries: Int): Result<List<GiphyImageItem>> {
        return mockReloadTrendingResult ?: Result.success(emptyList())
    }
}
