/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingUIState
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
class TrendingViewModel @Inject constructor(
    private val trendingRepository: TrendingRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageLoader: ImageLoader,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TrendingUIState> = MutableStateFlow(TrendingUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private var userPreferences: UserPreferences = UserPreferences(apiRequestLimit = null, rating = null)
    private var firstRefreshDone: Boolean = false

    init {
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

    fun getImageLoader() = imageLoader

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
            userPreferencesRepository.userPreferences.collect { prefs ->
                userPreferences = prefs
                if (userPreferences.isFullyConfigured() && !firstRefreshDone) {
                    Timber.tag("refresh").v("Got user preferences, trigger initial refresh")
                    refresh()
                    firstRefreshDone = true
                }
            }
        }
    }

    private fun startLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun loadTrendingData(apiMaxEntries: Int, rating: Rating) {
        viewModelScope.launch(dispatcher) {
            val repositoryResult = trendingRepository.reloadTrending(limit = apiMaxEntries, rating = rating)
            processTrendingList(repositoryResult = repositoryResult, isLoadingDone = true)
        }
    }

    private fun processTrendingList(repositoryResult: Result<List<GifObject>>, isLoadingDone: Boolean) {
        repositoryResult.fold(
            onSuccess = { gifObjects ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = !isLoadingDone,
                        gifObjects = gifObjects,
                    ).also {
                        Timber.tag("processTrendingList").v("Processed ${gifObjects.count()} entries, isLoading = ${!isLoadingDone}")
                    }
                }
            },
            onFailure = { exception ->
                updateUIForError("Error getting data: ${exception.message}")
            },
        )
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
