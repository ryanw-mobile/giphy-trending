package uk.ryanwong.giphytrending.data.repository

import androidx.lifecycle.LiveData
import io.reactivex.disposables.Disposable
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel

interface DefaultGiphyRepository {
    val trendingList: LiveData<List<GiphyImageItemDomainModel>>
    val showLoading: LiveData<Boolean>
    val errorMessage: LiveData<String?>

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
}