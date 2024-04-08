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
import com.rwmobi.giphytrending.domain.except
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GiphyRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val roomDbDataSource: RoomDbDataSource,
    private val dispatcher: CoroutineDispatcher,
) : GiphyRepository {
    override suspend fun fetchCachedTrending(): Result<List<GiphyImageItem>> {
        return withContext(dispatcher) {
            Result.runCatching {
                roomDbDataSource.queryData().toDomainModelList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun reloadTrending(apiMaxEntries: Int): Result<List<GiphyImageItem>> {
        return withContext(dispatcher) {
            try {
                roomDbDataSource.markDirty()
                Timber.tag("refreshTrending").v("Mark dirty: success")

                val trendingNetworkResponse = getTrendingFromNetwork(apiMaxEntries)
                roomDbDataSource.insertAllData(data = trendingNetworkResponse.trendingData.toTrendingEntityList())
                Timber.tag("refreshTrending").v("Insertion completed")

                val invalidationResult = invalidateDirtyTrendingDb()
                if (invalidationResult.isFailure) {
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

    private suspend fun getTrendingFromNetwork(apiMaxEntries: Int): TrendingNetworkResponse {
        return withContext(dispatcher) {
            networkDataSource.getTrending(
                apiKey = BuildConfig.GIPHY_API_KEY,
                limit = apiMaxEntries,
                rating = BuildConfig.API_RATING,
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
