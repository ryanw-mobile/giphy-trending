/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import javax.inject.Inject

class FakeUITestSearchRepository @Inject constructor() : SearchRepository {
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