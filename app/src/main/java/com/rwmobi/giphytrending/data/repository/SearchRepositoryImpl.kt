/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.source.local.mappers.asGiphyImageItem
import com.rwmobi.giphytrending.data.source.local.mappers.asTrendingEntity
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.di.GiphyApiKey
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    @GiphyApiKey private val giphyApiKey: String,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher, // Data source will use IODispatcher
) : SearchRepository {
    override suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GiphyImageItem>> {
        if (giphyApiKey.isBlank()) {
            @Suppress("UNREACHABLE_CODE")
            // It won't work without an API Key - CI might pass in nothing
            return Result.failure(exception = EmptyGiphyAPIKeyException())
        }

        if (keyword.isNullOrBlank()) {
            return Result.success(emptyList())
        }

        // Search changes frequently, we do not cache results
        return withContext(dispatcher) {
            try {
                val result = networkDataSource.getSearch(
                    apiKey = giphyApiKey,
                    keyword = keyword,
                    limit = limit,
                    offset = 0,
                    rating = rating.apiValue,
                )

                Result.success(value = result.trendingData.asTrendingEntity().asGiphyImageItem())
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (ex: Exception) {
                Timber.tag("search").e(ex)
                Result.failure(exception = ex)
            }
        }
    }
}
