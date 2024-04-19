/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.ui.destinations.search.SearchUIState
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
class SearchViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageLoader: ImageLoader,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchUIState> = MutableStateFlow(SearchUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private var userPreferences: UserPreferences = UserPreferences(apiRequestLimit = null, rating = null)
    private var initialisationDone: Boolean = false

    init {
        collectErrors()
        collectUserPreferences()
    }

    fun search(keyword: String?) {
        if (!userPreferences.isFullyConfigured()) {
            updateUIForError("Unable to access user preferences. Cannot refresh.")
            return
        }

        startLoading()
        val apiMaxEntries = userPreferences.apiRequestLimit
        val rating = userPreferences.rating

//        if (apiMaxEntries != null && rating != null) {
//            loadTrendingData(apiMaxEntries = apiMaxEntries, rating = rating)
//        } else {
//            updateUIForError("Preferences are not fully set.")
//        }
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
                _uiState.update {
                    addErrorMessage(
                        currentUiState = it,
                        message = preferenceErrors.localizedMessage ?: "Unknown error",
                    )
                }
            }
        }
    }

    private fun collectUserPreferences() {
        viewModelScope.launch(dispatcher) {
            userPreferencesRepository.userPreferences.collect { prefs ->
                userPreferences = prefs
                if (userPreferences.isFullyConfigured() && !initialisationDone) {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isLoading = false,
                        ).also {
                            Timber.tag("refresh").v("Got user preferences")
                        }
                    }
                    initialisationDone = true
                }
            }
        }
    }

    private fun startLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun updateUIForError(message: String) {
        _uiState.update {
            addErrorMessage(
                currentUiState = it,
                message = message,
            )
        }
    }

//    private fun loadTrendingData(apiMaxEntries: Int, rating: Rating) {
//        viewModelScope.launch(dispatcher) {
//            val repositoryResult = giphyRepository.reloadTrending(limit = apiMaxEntries, rating = rating)
//            processTrendingList(repositoryResult = repositoryResult, isLoadingDone = true)
//        }
//    }

    private fun processTrendingList(repositoryResult: Result<List<GiphyImageItem>>, isLoadingDone: Boolean) {
        when (repositoryResult.isFailure) {
            true -> updateUIForError("Error getting data: ${repositoryResult.exceptionOrNull()?.message}")
            false -> _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = !isLoadingDone,
                    giphyImageItems = repositoryResult.getOrNull() ?: emptyList(),
                ).also {
                    Timber.tag("processTrendingList").v("Processed ${repositoryResult.getOrNull()?.count() ?: 0} entries, isLoading = ${!isLoadingDone}")
                }
            }
        }
    }

    private fun addErrorMessage(currentUiState: SearchUIState, message: String): SearchUIState {
        val newErrorMessage = ErrorMessage(
            id = UUID.randomUUID().mostSignificantBits,
            message = message,
        )
        return currentUiState.copy(
            isLoading = false,
            errorMessages = currentUiState.errorMessages + newErrorMessage,
        )
    }
}
