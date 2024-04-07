/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.ui.models.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SettingsUIState> = MutableStateFlow(SettingsUIState(isLoading = true))
    var uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            launch {
                userPreferencesRepository.preferenceErrors.collect { preferenceErrors ->
                    Timber.e(preferenceErrors)

                    _uiState.update { currentUiState ->
                        val errorMessages = currentUiState.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            message = preferenceErrors.localizedMessage ?: "Unknown error",
                        )
                        currentUiState.copy(
                            isLoading = false,
                            errorMessages = errorMessages,
                        )
                    }
                }
            }

            launch {
                userPreferencesRepository.apiMaxEntries.collect { apiMaxEntries ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isLoading = apiMaxEntries == null,
                            apiMaxEntries = apiMaxEntries,
                        )
                    }
                }
            }
        }
    }

    fun setApiMax(maxApiEntries: Int) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = false,
                    apiMaxEntries = maxApiEntries,
                )
            }
            userPreferencesRepository.setApiMax(apiMax = maxApiEntries)
        }
    }

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
}
