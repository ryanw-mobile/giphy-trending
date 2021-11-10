package uk.ryanwong.giphytrending.ui.trending

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import uk.ryanwong.giphytrending.data.repository.TrendingRepository
import uk.ryanwong.giphytrending.di.DaggerAppComponent
import javax.inject.Inject

class TrendingViewModel : ViewModel() {

    @Inject
    lateinit var repository: TrendingRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    // This is to maintain the recyclerview scrolling state during list refresh
    private var _listState: Parcelable? = null
    val listState: Parcelable?
        get() = _listState

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }

    init {
        DaggerAppComponent.create().inject(this)
        refreshList()
    }

    fun refreshList() {
        compositeDisposable.add(repository.fetchDataFromDatabase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}