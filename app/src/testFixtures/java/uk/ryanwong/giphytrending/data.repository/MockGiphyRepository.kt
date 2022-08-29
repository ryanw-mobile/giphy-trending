package uk.ryanwong.giphytrending.data.repository

import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel

class MockGiphyRepository : GiphyRepository {
    var mockFetchCachedTrendingResult: Result<List<GiphyImageItemDomainModel>>? = null
    override suspend fun fetchCachedTrending(): Result<List<GiphyImageItemDomainModel>> {
        return mockFetchCachedTrendingResult ?: Result.success(emptyList())
    }

    var mockReloadTrendingResult: Result<List<GiphyImageItemDomainModel>>? = null
    override suspend fun reloadTrending(apiMaxEntries: Int): Result<List<GiphyImageItemDomainModel>> {
        return mockReloadTrendingResult ?: Result.success(emptyList())
    }
}
