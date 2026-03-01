/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.repository.mappers.toEntity
import com.rwmobi.giphytrending.data.repository.mappers.toGifObject
import com.rwmobi.giphytrending.data.source.local.interfaces.DatabaseDataSource
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    @GiphyApiKey private val giphyApiKey: String,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : TrendingRepository {
    override val trendingFlow: Flow<List<GifObject>> = databaseDataSource.queryData()
        .map { entities -> entities.map { it.toGifObject() } }

    override suspend fun refreshTrending(limit: Int, rating: Rating): Result<Unit> {
        if (giphyApiKey.isBlank()) {
            return Result.failure(exception = EmptyGiphyAPIKeyException())
        }

        return withContext(dispatcher) {
            Result.runCatching {
                databaseDataSource.markDirty()
                Timber.tag("refreshTrending").v("Mark dirty: success")

                val trendingNetworkResponse = getTrendingFromNetwork(limit = limit, rating = rating)
                databaseDataSource.insertAllData(data = trendingNetworkResponse.trendingData.map { it.toEntity() })
                Timber.tag("refreshTrending").v("Insertion completed")

                val invalidationResult = invalidateDirtyTrendingDb()
                invalidationResult.getOrThrow()
            }.except<CancellationException, _>()
        }
    }

    private suspend fun getTrendingFromNetwork(limit: Int, rating: Rating): TrendingNetworkResponseDto = withContext(dispatcher) {
        networkDataSource.getTrending(
            apiKey = giphyApiKey,
            limit = limit,
            offset = (0..5).random(), // Eye candie to make every refresh different
            rating = rating.apiValue,
        )
    }

    private suspend fun invalidateDirtyTrendingDb(): Result<Unit> = withContext(dispatcher) {
        Result.runCatching {
            databaseDataSource.deleteDirty()
            Timber.tag("invalidateDirtyTrendingDb").v("success")
        }.except<CancellationException, _>()
    }
}
