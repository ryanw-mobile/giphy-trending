/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsUIState
import com.rwmobi.giphytrending.ui.model.ErrorMessage
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
                userPreferencesRepository.userPreferences.collect { userPreferences ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isLoading = !userPreferences.isFullyConfigured(),
                            apiRequestLimit = userPreferences.apiRequestLimit,
                            rating = userPreferences.rating,
                        )
                    }
                }
            }
        }
    }

    fun setApiRequestLimit(limit: Int) {
        viewModelScope.launch(dispatcher) {
            userPreferencesRepository.setApiRequestLimit(limit = limit)

            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = false,
                    apiRequestLimit = limit,
                )
            }
        }
    }

    fun setRating(rating: Rating) {
        viewModelScope.launch(dispatcher) {
            userPreferencesRepository.setRating(rating = rating)

            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = false,
                    rating = rating,
                )
            }
        }
    }

    fun requestScrollToTop(enabled: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestScrollToTop = enabled,
            )
        }
    }

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
}
