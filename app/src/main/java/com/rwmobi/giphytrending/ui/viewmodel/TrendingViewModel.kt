/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.domain.usecase.GetTrendingFlowUseCase
import com.rwmobi.giphytrending.domain.usecase.GetUserPreferencesUseCase
import com.rwmobi.giphytrending.domain.usecase.RefreshTrendingUseCase
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingEffect
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingUIState
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
class TrendingViewModel @Inject constructor(
    private val getTrendingFlowUseCase: GetTrendingFlowUseCase,
    private val refreshTrendingUseCase: RefreshTrendingUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val userPreferencesRepository: UserPreferencesRepository, // Keep for preferenceErrors
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TrendingUIState> = MutableStateFlow(TrendingUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TrendingEffect>()
    val effect = _effect.asSharedFlow()

    private var userPreferences: UserPreferences = UserPreferences(apiRequestLimit = null, rating = null)

    init {
        collectTrendingGifs()
        collectErrors()
        collectUserPreferences()
    }

    fun refresh() {
        if (!userPreferences.isFullyConfigured()) {
            updateUIForError("Unable to access user preferences. Cannot refresh.")
            return
        }

        startLoading()
        val apiMaxEntries = userPreferences.apiRequestLimit
        val rating = userPreferences.rating

        if (apiMaxEntries != null && rating != null) {
            loadTrendingData(apiMaxEntries = apiMaxEntries, rating = rating)
        } else {
            updateUIForError("Preferences are not fully set.")
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
            _effect.emit(TrendingEffect.ShowSnackbar(message))
        }
    }

    private fun collectTrendingGifs() {
        viewModelScope.launch(dispatcher) {
            getTrendingFlowUseCase().collect { gifObjects ->
                _uiState.update { it.copy(gifObjects = gifObjects) }
            }
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
            getUserPreferencesUseCase().collect { prefs ->
                val previousPrefs = userPreferences
                userPreferences = prefs
                if (userPreferences.isFullyConfigured()) {
                    if (previousPrefs != userPreferences) {
                        Timber.tag("refresh").v("Got new user preferences, trigger refresh")
                        refresh()
                    }
                }
            }
        }
    }

    private fun startLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun loadTrendingData(apiMaxEntries: Int, rating: Rating) {
        viewModelScope.launch(dispatcher) {
            val repositoryResult = refreshTrendingUseCase(limit = apiMaxEntries, rating = rating)
            _uiState.update { it.copy(isLoading = false) }
            repositoryResult.onFailure { exception ->
                updateUIForError("Error getting data: ${exception.message}")
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
