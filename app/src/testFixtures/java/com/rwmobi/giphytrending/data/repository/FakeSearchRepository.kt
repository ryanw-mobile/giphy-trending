package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository

class FakeSearchRepository : SearchRepository {
    var searchResult: Result<List<GiphyImageItem>>? = null

    override suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GiphyImageItem>> {
        return searchResult ?: Result.success(emptyList())
    }
}
