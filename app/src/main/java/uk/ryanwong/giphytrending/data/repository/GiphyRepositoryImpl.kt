package uk.ryanwong.giphytrending.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import uk.ryanwong.giphytrending.BuildConfig
import uk.ryanwong.giphytrending.data.source.local.GiphyDatabase
import uk.ryanwong.giphytrending.data.source.local.toDomainModelList
import uk.ryanwong.giphytrending.data.source.local.toTrendingEntityList
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.model.TrendingNetworkResponse
import uk.ryanwong.giphytrending.di.IoDispatcher
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel
import uk.ryanwong.giphytrending.except
import javax.inject.Inject

class GiphyRepositoryImpl @Inject constructor(
    private val giphyApiService: GiphyApi,
    private val giphyDatabase: GiphyDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GiphyRepository {
    /**
     * Note:
     * The current use of Result() is not the best option. I should have derived my own Result class.
     * Greg would say I need error messages showing more specific problem,
     * probably would need to have some more user friendly error messages, too.
     * Given this is a demo app, I will refine later if I have time.
     * I apologise, Greg.
     */

    /**
     * Does not trigger network calls
     */
    override suspend fun fetchCachedTrending(): Result<List<GiphyImageItemDomainModel>> {
        return withContext(dispatcher) {
            Result.runCatching {
                giphyDatabase.trendingDao().queryData().toDomainModelList()
            }.except<CancellationException, _>()
        }
    }

    /***
     * Trigger network calls, cache data to local database and return the results to caller
     */
    override suspend fun reloadTrending(apiMaxEntries: Int): Result<List<GiphyImageItemDomainModel>> {
        return withContext(dispatcher) {
            try {
                // Mark existing contents dirty. After successful API call old entries will be removed
                giphyDatabase.trendingDao().markDirty()
                Timber.v("reloadTrending - mark dirty: success")

                val trendingNetworkResponse = getTrendingFromNetwork(apiMaxEntries)
                giphyDatabase.trendingDao()
                    .insertAllData(data = trendingNetworkResponse.trendingData.toTrendingEntityList())
                Timber.v("reloadTrending: insertion completed")

                val invalidationResult = invalidateDirtyTrendingDb()
                if (invalidationResult.isFailure) {
                    // Error propagation is not ideal, but simplified in this demo app. Greg please don't blame me
                    Result.failure(
                        exception = invalidationResult.exceptionOrNull() ?: UnknownError()
                    )
                } else {
                    Result.success(
                        value = giphyDatabase.trendingDao().queryData().toDomainModelList()
                    )
                }
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (ex: Exception) {
                Timber.e("refreshTrending: ${ex.message}")
                Result.failure(exception = ex)
            }
        }
    }

    private suspend fun getTrendingFromNetwork(apiMaxEntries: Int): TrendingNetworkResponse {
        return withContext(dispatcher) {
            giphyApiService.getTrending(
                BuildConfig.GIPHY_API_KEY, apiMaxEntries,
                BuildConfig.API_RATING
            )
        }
    }

    private suspend fun invalidateDirtyTrendingDb(): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                giphyDatabase.trendingDao().deleteDirty()
                Timber.v("invalidateDirtyTrendingDb: success")
            }.except<CancellationException, _>()
        }
    }
}