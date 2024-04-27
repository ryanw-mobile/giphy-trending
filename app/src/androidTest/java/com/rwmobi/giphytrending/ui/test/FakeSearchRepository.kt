package com.rwmobi.giphytrending.ui.test

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import javax.inject.Inject

internal class FakeSearchRepository @Inject constructor() : SearchRepository {
    private var searchResult: Result<List<GiphyImageItem>>? = null
    private var lastSuccessfulSearchKeyword: String? = null
    private var lastSuccessfulSearchResults: List<GiphyImageItem>? = null

    override suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GiphyImageItem>> {
        return searchResult ?: Result.success(emptyList())
    }

    override fun getLastSuccessfulSearchKeyword(): String? = lastSuccessfulSearchKeyword
    override fun getLastSuccessfulSearchResults(): List<GiphyImageItem>? = lastSuccessfulSearchResults

    fun setSearchResultForTest(searchResult: Result<List<GiphyImageItem>>?) {
        this.searchResult = searchResult
    }

    fun setLastSuccessfulSearchKeywordForTest(lastSuccessfulSearchKeyword: String?) {
        this.lastSuccessfulSearchKeyword = lastSuccessfulSearchKeyword
    }

    fun setLastSuccessfulSearchResultsForTest(lastSuccessfulSearchResults: List<GiphyImageItem>?) {
        this.lastSuccessfulSearchResults = lastSuccessfulSearchResults
    }
}
