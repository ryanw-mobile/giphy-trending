package uk.ryanwong.giphytrending.ui.trending

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.ryanwong.giphytrending.GiphyApplication
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import javax.inject.Inject

class TrendingViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    // UI should not interact with repository directly
    val trendingList = giphyRepository.trendingList
    val errorMessage = giphyRepository.errorMessage
    val showLoading = giphyRepository.showLoading
    val showNoData: LiveData<Boolean> = Transformations.map(trendingList) { list ->
        !showLoading.value!! && list.isEmpty()
    }

    // This is to maintain the recyclerview scrolling state during list refresh
    // No practical use for this UI layout because we have to back to top for swipe to refresh
    private var _listState: Parcelable? = null
    val listState: Parcelable?
        get() = _listState

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }

    init {
        GiphyApplication.appComponent.inject(this)
        fetchDataFromDatabase() // get cached data first. Fragment should call refresh when view is ready
    }

    fun refresh() =
        viewModelScope.launch {
            userPreferencesRepository.getApiMax().collect() { apiMaxEntries ->
                Timber.v("refresh requesting $apiMaxEntries entries from the repository")
                compositeDisposable.add(
                    giphyRepository.refreshTrending(apiMaxEntries)
                )
            }
        }

    private fun fetchDataFromDatabase() {
        compositeDisposable.add(giphyRepository.fetchTrending())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}