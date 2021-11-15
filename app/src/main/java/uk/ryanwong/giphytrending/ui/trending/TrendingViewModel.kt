package uk.ryanwong.giphytrending.ui.trending

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import uk.ryanwong.giphytrending.GiphyApplication
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import javax.inject.Inject

class TrendingViewModel @Inject constructor(private val repository: GiphyRepository) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    // UI should not interact with repository directly
    val trendingList = repository.trendingList
    val errorMessage = repository.errorMessage
    val showLoading = repository.showLoading
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
        fetchDataFromDatabase()
        refresh()
    }

    fun refresh() {
        compositeDisposable.add(repository.refreshTrending())
    }

    private fun fetchDataFromDatabase() {
        compositeDisposable.add(repository.fetchTrending())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}