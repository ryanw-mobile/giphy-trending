package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository

class FakeSearchRepository : SearchRepository {
    private var searchResult: Result<List<GifObject>>? = null
    private var lastSuccessfulSearchKeyword: String? = null
    private var lastSuccessfulSearchResults: List<GifObject>? = null

    override suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>> {
        return searchResult ?: Result.success(emptyList())
    }

    override fun getLastSuccessfulSearchKeyword(): String? = lastSuccessfulSearchKeyword
    override fun getLastSuccessfulSearchResults(): List<GifObject>? = lastSuccessfulSearchResults

    fun setSearchResultForTest(searchResult: Result<List<GifObject>>?) {
        this.searchResult = searchResult
    }

    fun setLastSuccessfulSearchKeywordForTest(lastSuccessfulSearchKeyword: String?) {
        this.lastSuccessfulSearchKeyword = lastSuccessfulSearchKeyword
    }

    fun setLastSuccessfulSearchResultsForTest(lastSuccessfulSearchResults: List<GifObject>?) {
        this.lastSuccessfulSearchResults = lastSuccessfulSearchResults
    }
}
