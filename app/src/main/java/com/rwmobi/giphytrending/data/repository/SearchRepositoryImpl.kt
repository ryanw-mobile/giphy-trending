/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.repository.mappers.toGifObject
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.di.GiphyApiKey
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.exceptions.except
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * While the data layer, including data sources, is responsible for managing the actual storage and retrieval of data,
 * the repository serves as a boundary between the data layer and the rest of the application.
 * Storing data in variables within the repository can be seen as a pragmatic approach to caching within the context
 * of Clean Architecture, providing a balance between simplicity, maintainability, and flexibility.
 * -- ChatGPT
 */

class SearchRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    @GiphyApiKey private val giphyApiKey: String,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : SearchRepository {
    private var lastSuccessfulSearchKeyword: String? = null
    private var lastSuccessfulSearchResults: List<GifObject>? = null

    override suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>> {
        if (giphyApiKey.isBlank()) {
            @Suppress("UNREACHABLE_CODE")
            // It won't work without an API Key - CI might pass in nothing
            return Result.failure(exception = EmptyGiphyAPIKeyException())
        }

        if (keyword.isNullOrBlank()) {
            return Result.success(emptyList())
        }

        // Search changes frequently, we only cache results in memory
        return withContext(dispatcher) {
            runCatching {
                val result = networkDataSource.getSearch(
                    apiKey = giphyApiKey,
                    keyword = keyword,
                    limit = limit,
                    offset = 0,
                    rating = rating.apiValue,
                )

                lastSuccessfulSearchKeyword = keyword
                lastSuccessfulSearchResults = result.trendingData.map { it.toGifObject() }
                lastSuccessfulSearchResults ?: emptyList()
            }.except<CancellationException, _>()
        }
    }

    override fun getLastSuccessfulSearchKeyword(): String? = lastSuccessfulSearchKeyword
    override fun getLastSuccessfulSearchResults(): List<GifObject>? = lastSuccessfulSearchResults
}
