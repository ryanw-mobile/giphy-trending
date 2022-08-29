package uk.ryanwong.giphytrending.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ryanwong.giphytrending.BuildConfig
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import uk.ryanwong.giphytrending.di.MainDispatcher
import javax.inject.Inject
import kotlin.math.max

// Minimum entries to request from server
private const val API_MIN = 50

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _settingsUIState: MutableStateFlow<SettingsUIState> =
        MutableStateFlow(SettingsUIState.Ready)
    var settingsUIState: StateFlow<SettingsUIState> = _settingsUIState

    private val _apiMaxEntriesProgress: MutableStateFlow<Int> = MutableStateFlow(0)
    var apiMaxEntriesProgress: StateFlow<Int> = _apiMaxEntriesProgress

    // The seekbar progress has to deduct the minimum value
    fun getApiMax() {
        viewModelScope.launch(dispatcher) {
            val apiMaxResponse = userPreferencesRepository.getApiMax()

            val apiMax = apiMaxResponse.getOrNull() ?: BuildConfig.API_MAX_ENTRIES.toInt()
            _apiMaxEntriesProgress.value = max(apiMax.minus(API_MIN), 0)
            updateUIState(repositoryResult = apiMaxResponse)
        }
    }

    fun translateMaxApiEntries(value: Int) = value.plus(API_MIN).toString()

    fun setApiMax(maxApiEntries: Int) {
        viewModelScope.launch(dispatcher) {
            updateUIState(
                repositoryResult = userPreferencesRepository.setApiMax(maxApiEntries.plus(API_MIN))
            )
        }
    }

    private fun updateUIState(repositoryResult: Result<*>) {
        when {
            repositoryResult.isFailure -> {
                // TODO: Can have a better Result class and error message.
                _settingsUIState.value =
                    SettingsUIState.Error(errMsg = "Error accessing preferences: {${repositoryResult.exceptionOrNull()?.message}")
                repositoryResult.exceptionOrNull()?.printStackTrace()
            }
            repositoryResult.isSuccess -> {
                _settingsUIState.value = SettingsUIState.Ready
            }
        }
    }

    fun notifyErrorMessageDisplayed() {
        _settingsUIState.value = SettingsUIState.Ready
    }
}
