/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.SearchRepository
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
    private val searchRepository: SearchRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    // API returns HTTP 414 if query string longer than this
    private val keywordMaxLength = 50
    private val _uiState: MutableStateFlow<SearchUIState> = MutableStateFlow(
        SearchUIState(
            isLoading = true,
            keywordMaxLength = keywordMaxLength,
        ),
    )
    val uiState = _uiState.asStateFlow()

    private var userPreferences: UserPreferences = UserPreferences(apiRequestLimit = null, rating = null)
    private var initialisationDone: Boolean = false
    private var keyword: String = ""

    init {
        collectErrors()
        collectUserPreferences()
    }

    fun fetchLastSuccessfulSearch() {
        startLoading()
        viewModelScope.launch(dispatcher) {
            val lastSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
            val lastSearchResult = searchRepository.getLastSuccessfulSearchResults()

            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = false,
                    keyword = lastSearchKeyword ?: "",
                    gifObjects = lastSearchResult,
                )
            }
        }
    }

    fun updateKeyword(keyword: String?) {
        // caller is not allowed to feed us null value, which is reserved to the ViewModel
        this.keyword = keyword?.take(keywordMaxLength) ?: ""

        _uiState.update { currentUiState ->
            currentUiState.copy(
                keyword = this.keyword,
            )
        }
    }

    fun clearKeyword() {
        keyword = ""

        _uiState.update { currentUiState ->
            currentUiState.copy(
                keyword = keyword,
            )
        }
    }

    fun search() {
        startLoading()
        val apiMaxEntries = userPreferences.apiRequestLimit
        val rating = userPreferences.rating

        if (apiMaxEntries != null && rating != null) {
            // This forces the scrolling to the top of the list
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = true,
                    gifObjects = emptyList(),
                )
            }
            startNewSearch(keyword = keyword, apiMaxEntries = apiMaxEntries, rating = rating)
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

    private fun startNewSearch(keyword: String?, apiMaxEntries: Int, rating: Rating) {
        viewModelScope.launch(dispatcher) {
            val searchResult = searchRepository.search(keyword = keyword, limit = apiMaxEntries, rating = rating)
            processTrendingList(repositoryResult = searchResult)
        }
    }

    private fun processTrendingList(repositoryResult: Result<List<GifObject>>) {
        repositoryResult.fold(
            onSuccess = { gifObjects ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        gifObjects = gifObjects,
                    ).also {
                        Timber.tag("processTrendingList").v("Processed ${gifObjects.count()} entries")
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
