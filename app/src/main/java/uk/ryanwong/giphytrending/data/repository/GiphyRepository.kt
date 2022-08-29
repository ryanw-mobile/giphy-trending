package uk.ryanwong.giphytrending.data.repository

import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel

interface GiphyRepository {
    /***
     * Fetch and expose trending list
     * Repository currently retrieves cached data from local database.
     * This can be changed without acknowledging to the callers.
     */
    suspend fun fetchCachedTrending(): Result<List<GiphyImageItemDomainModel>>

    /***
     * Request the repository to refresh and update the exposed trending list
     * Repository currently run RestAPI calls, cache data to local database,
     * and return the cached contents.
     */
    suspend fun reloadTrending(apiMaxEntries: Int): Result<List<GiphyImageItemDomainModel>>
}
