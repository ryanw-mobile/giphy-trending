/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.domain.usecase.GetUserPreferencesUseCase
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsEffect
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsUIState
import com.rwmobi.giphytrending.ui.model.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsUIState> = MutableStateFlow(SettingsUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        collectErrors()
        collectUserPreferences()
    }

    fun setApiRequestLimit(limit: Int) {
        viewModelScope.launch(dispatcher) {
            userPreferencesRepository.setApiRequestLimit(limit = limit)
        }
    }

    fun setRating(rating: Rating) {
        viewModelScope.launch(dispatcher) {
            userPreferencesRepository.setRating(rating = rating)
        }
    }

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun requestScrollToTop(enabled: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestScrollToTop = enabled,
            )
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch(dispatcher) {
            _effect.emit(SettingsEffect.ShowSnackbar(message))
        }
    }

    private fun collectErrors() {
        viewModelScope.launch(dispatcher) {
            userPreferencesRepository.preferenceErrors.collect { preferenceErrors ->
                Timber.e(preferenceErrors)
                updateUIForError(message = preferenceErrors.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun collectUserPreferences() {
        viewModelScope.launch(dispatcher) {
            getUserPreferencesUseCase().collect { userPreferences ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        apiRequestLimit = userPreferences.apiRequestLimit,
                        rating = userPreferences.rating,
                    )
                }
            }
        }
    }

    private fun updateUIForError(message: String) {
        _uiState.update { currentUiState ->
            val newErrorMessages = if (_uiState.value.errorMessages.any { it.message == message }) {
                currentUiState.errorMessages
            } else {
                currentUiState.errorMessages + ErrorMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    message = message,
                )
            }
            currentUiState.copy(
                isLoading = false,
                errorMessages = newErrorMessages,
            )
        }
    }
}
