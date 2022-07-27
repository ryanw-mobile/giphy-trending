package uk.ryanwong.giphytrending.ui.trending

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import uk.ryanwong.giphytrending.di.MainDispatcher
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel
import uk.ryanwong.giphytrending.ui.TrendingUIState
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _trendingUIState: MutableStateFlow<TrendingUIState> =
        MutableStateFlow(TrendingUIState.Ready)
    var trendingUIState: StateFlow<TrendingUIState> = _trendingUIState

    private val _trendingList: MutableStateFlow<List<GiphyImageItemDomainModel>> =
        MutableStateFlow(emptyList())
    var trendingList: StateFlow<List<GiphyImageItemDomainModel>> = _trendingList

    // This is to maintain the recyclerview scrolling state during list refresh
    private var _listState: Parcelable? = null
    val listState: Parcelable? = _listState

    init {
        viewModelScope.launch(dispatcher) {
            // get cached data first. Fragment should call refresh when view is ready
            processTrendingList(repositoryResult = giphyRepository.fetchCachedTrending())
        }
    }

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }


    fun refresh() {
        viewModelScope.launch(dispatcher) {
            _trendingUIState.value = TrendingUIState.Loading
            val apiMaxEntries = 1// userPreferencesRepository.getApiMax().collect()
            Timber.v("refresh requesting $apiMaxEntries entries from the repository")

            processTrendingList(repositoryResult = giphyRepository.reloadTrending(apiMaxEntries))
        }
    }

    private fun processTrendingList(repositoryResult: Result<List<GiphyImageItemDomainModel>>) {
        when {
            repositoryResult.isFailure -> {
                // TODO: Can have a better Result class and error message.
                _trendingUIState.value =
                    TrendingUIState.Error(errMsg = "Error getting data: {${repositoryResult.exceptionOrNull()?.message}")
            }
            repositoryResult.isSuccess -> {
                _trendingList.value = repositoryResult.getOrNull() ?: emptyList()
                _trendingUIState.value = TrendingUIState.Ready
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _trendingList.value = emptyList()
    }

    fun notifyErrorMessageDisplayed() {
        _trendingUIState.value = TrendingUIState.Ready
    }
}