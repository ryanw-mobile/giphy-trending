package uk.ryanwong.giphytrending.data.repository

import io.reactivex.disposables.Disposable
import io.reactivex.subscribers.DisposableSubscriber
import uk.ryanwong.giphytrending.data.source.network.model.TrendingNetworkModel

interface GiphyRepository {
    /***
     * Fetch and expose trending list
     * Repository currently retrieves cached data from local database.
     * This can be changed without acknowledging to the callers.
     */
    fun fetchTrending(): Disposable

    /***
     * Request the repository to refresh and update the exposed trending list
     * Repository currently run RestAPI calls, cache data to local database,
     * and return the cached contents.
     */
    fun refreshTrending(apiMaxEntries: Int): Disposable
    fun getTrendingFromNetwork(apiMaxEntries: Int): Disposable
    fun cacheTrendingToDb(): DisposableSubscriber<TrendingNetworkModel>
    fun invalidateDirtyTrendingDb(): Disposable
}