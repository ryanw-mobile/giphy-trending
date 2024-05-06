/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
import com.rwmobi.giphytrending.data.source.local.mappers.toGifObject
import com.rwmobi.giphytrending.data.source.local.mappers.toTrendingEntity
import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.di.GiphyApiKey
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.exceptions.except
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    @GiphyApiKey private val giphyApiKey: String,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : TrendingRepository {
    override suspend fun fetchCachedTrending(): Result<List<GifObject>> {
        return withContext(dispatcher) {
            Result.runCatching {
                databaseDataSource.queryData().toGifObject()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GifObject>> {
        if (giphyApiKey.isBlank()) {
            @Suppress("UNREACHABLE_CODE")
            // It won't work without an API Key - CI might pass in nothing
            return Result.failure(exception = EmptyGiphyAPIKeyException())
        }

        return withContext(dispatcher) {
            try {
                databaseDataSource.markDirty()
                Timber.tag("refreshTrending").v("Mark dirty: success")

                val trendingNetworkResponse = getTrendingFromNetwork(limit = limit, rating = rating)
                databaseDataSource.insertAllData(data = trendingNetworkResponse.trendingData.toTrendingEntity())
                Timber.tag("refreshTrending").v("Insertion completed")

                val invalidationResult = invalidateDirtyTrendingDb()
                if (invalidationResult.isFailure) {
                    Timber.tag("invalidationResult").e(invalidationResult.exceptionOrNull())
                    Result.failure(exception = invalidationResult.exceptionOrNull() ?: UnknownError())
                } else {
                    Result.success(value = databaseDataSource.queryData().toGifObject())
                }
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (ex: Exception) {
                Timber.tag("refreshTrending").e(ex)
                Result.failure(exception = ex)
            }
        }
    }

    private suspend fun getTrendingFromNetwork(limit: Int, rating: Rating): TrendingNetworkResponseDto {
        return withContext(dispatcher) {
            networkDataSource.getTrending(
                apiKey = giphyApiKey,
                limit = limit,
                offset = (0..5).random(), // Eye candie to make every refresh different
                rating = rating.apiValue,
            )
        }
    }

    private suspend fun invalidateDirtyTrendingDb(): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                databaseDataSource.deleteDirty()
                Timber.tag("invalidateDirtyTrendingDb").v("success")
            }.except<CancellationException, _>()
        }
    }
}
