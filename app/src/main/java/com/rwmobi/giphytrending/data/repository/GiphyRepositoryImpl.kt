/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.data.source.local.RoomDbDataSource
import com.rwmobi.giphytrending.data.source.local.toDomainModelList
import com.rwmobi.giphytrending.data.source.local.toTrendingEntityList
import com.rwmobi.giphytrending.data.source.network.NetworkDataSource
import com.rwmobi.giphytrending.data.source.network.model.TrendingNetworkResponse
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.exceptions.except
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GiphyRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val roomDbDataSource: RoomDbDataSource,
    private val giphyApiKey: String = BuildConfig.GIPHY_API_KEY,
    private val dispatcher: CoroutineDispatcher,
) : GiphyRepository {
    override suspend fun fetchCachedTrending(): Result<List<GiphyImageItem>> {
        return withContext(dispatcher) {
            Result.runCatching {
                roomDbDataSource.queryData().toDomainModelList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GiphyImageItem>> {
        if (giphyApiKey.isBlank()) {
            @Suppress("UNREACHABLE_CODE")
            // It won't work without an API Key - CI might pass in nothing
            return Result.failure(
                exception = throw EmptyGiphyAPIKeyException(),
            )
        }

        return withContext(dispatcher) {
            try {
                roomDbDataSource.markDirty()
                Timber.tag("refreshTrending").v("Mark dirty: success")

                val trendingNetworkResponse = getTrendingFromNetwork(limit = limit, rating = rating)
                roomDbDataSource.insertAllData(data = trendingNetworkResponse.trendingData.toTrendingEntityList())
                Timber.tag("refreshTrending").v("Insertion completed")

                val invalidationResult = invalidateDirtyTrendingDb()
                if (invalidationResult.isFailure) {
                    Timber.tag("invalidationResult").e(invalidationResult.exceptionOrNull())
                    Result.failure(
                        exception = invalidationResult.exceptionOrNull() ?: UnknownError(),
                    )
                } else {
                    Result.success(
                        value = roomDbDataSource.queryData().toDomainModelList(),
                    )
                }
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (ex: Exception) {
                Timber.tag("refreshTrending").e(ex)
                Result.failure(exception = ex)
            }
        }
    }

    private suspend fun getTrendingFromNetwork(limit: Int, rating: Rating): TrendingNetworkResponse {
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
                roomDbDataSource.deleteDirty()
                Timber.tag("invalidateDirtyTrendingDb").v("success")
            }.except<CancellationException, _>()
        }
    }
}
