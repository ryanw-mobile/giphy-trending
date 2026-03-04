/*
 * Copyright 2024-2026 RW MobiMedia UK Limited
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import javax.inject.Inject

class FakeUITestSearchRepository @Inject constructor() : SearchRepository {
    private var searchResult: Result<List<GifObject>>? = null
    private var lastSuccessfulSearchKeyword: String? = null
    private var lastSuccessfulSearchResults: List<GifObject>? = null

    override suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>> {
        val result = searchResult ?: Result.success(emptyList())
        if (result.isSuccess) {
            lastSuccessfulSearchKeyword = keyword
            lastSuccessfulSearchResults = result.getOrNull()
        }
        return result
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
