package uk.ryanwong.giphytrending.data.repository

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel

class FakeGiphyGiphyRepository() : DefaultGiphyRepository {

    private var _trendingList = MutableLiveData<List<GiphyImageItemDomainModel>>(listOf())
    override val trendingList
        get() = _trendingList

    private var _showLoading = MutableLiveData(false)
    override val showLoading
        get() = _showLoading

    private var _errorMessage = MutableLiveData<String>()
    override val errorMessage
        get() = _errorMessage

    /***
     * Fetch and expose trending list
     * Repository currently retrieves cached data from local database.
     * This can be changed without acknowledging to the callers.
     */
    override fun fetchTrending(): Disposable {
        TODO("Not yet implemented")
    }

    /***
     * Request the repository to refresh and update the exposed trending list
     * Repository currently run RestAPI calls, cache data to local database,
     * and return the cached contents.
     */
    override fun refreshTrending(apiMaxEntries: Int): Disposable {
        TODO("Not yet implemented")
    }
}