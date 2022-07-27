package uk.ryanwong.giphytrending.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import uk.ryanwong.giphytrending.di.MainDispatcher
import javax.inject.Inject

// Minimum entries to request from server
private const val API_MIN = 50

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserPreferencesRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _apiMaxEntriesProgress: MutableStateFlow<Int> = MutableStateFlow(0)
    var apiMaxEntriesProgress: StateFlow<Int> = _apiMaxEntriesProgress

    // The seekbar progress has to deduct the minimum value
//    val apiMaxEntriesProgress = repository.apiMaxEntries.map { apiMax ->
//        max(apiMax.minus(API_MIN), 0)
//    }

    fun translateMaxApiEntries(value: Int) = value.plus(API_MIN).toString()

    fun setApiMax(maxApiEntries: Int) {
        viewModelScope.launch(dispatcher) {
            repository.setApiMax(maxApiEntries.plus(API_MIN))
        }
    }
}