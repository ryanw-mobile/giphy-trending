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
    private val giphyRepository: GiphyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageLoader: ImageLoader,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TrendingUIState> = MutableStateFlow(TrendingUIState(isLoading = true))
    var uiState = _uiState.asStateFlow()

    private var userPreferences: UserPreferences = UserPreferences(apiRequestLimit = null, rating = null)
    private var firstRefreshDone: Boolean = false

    init {
        viewModelScope.launch(dispatcher) {
            processTrendingList(
                repositoryResult = giphyRepository.fetchCachedTrending(),
                isLoadingDone = false,
            )

            launch {
                userPreferencesRepository.preferenceErrors.collect { preferenceErrors ->
                    Timber.e(preferenceErrors)

                    _uiState.update { currentUiState ->
                        val errorMessages = currentUiState.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            message = preferenceErrors.localizedMessage ?: "Unknown error",
                        )
                        currentUiState.copy(
                            errorMessages = errorMessages,
                        )
                    }
                }
            }

            launch {
                userPreferencesRepository.userPreferences.collect {
                    userPreferences = it
                    if (userPreferences.isFullyConfigured() && !firstRefreshDone) {
                        Timber.tag("refresh").v("got user preferences, trigger initial refresh")
                        refresh()
                        firstRefreshDone = true
                    }
                }
            }
        }
    }

    fun refresh() {
        if (!userPreferences.isFullyConfigured()) {
            _uiState.update { currentUiState ->
                val errorMessages = currentUiState.errorMessages + ErrorMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    message = "Unable to access user preferences. Cannot refresh.",
                )
                currentUiState.copy(
                    isLoading = false,
                    errorMessages = errorMessages,
                )
            }
            return
        }

        // Safe local variables after the check
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        val apiMaxEntries = userPreferences.apiRequestLimit
        val rating = userPreferences.rating

        if (apiMaxEntries != null && rating != null) {
            viewModelScope.launch(dispatcher) {
                Timber.tag("refresh").v("Requesting $apiMaxEntries entries with rating = ${rating.apiValue}")
                processTrendingList(
                    repositoryResult = giphyRepository.reloadTrending(limit = apiMaxEntries, rating = rating),
                    isLoadingDone = true,
                )
            }
        }
    }

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun getImageLoader() = imageLoader

    private fun processTrendingList(repositoryResult: Result<List<GiphyImageItem>>, isLoadingDone: Boolean) {
        when {
            repositoryResult.isFailure -> {
                _uiState.update { currentUiState ->
                    val errorMessages = currentUiState.errorMessages + ErrorMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        message = "Error getting data: ${repositoryResult.exceptionOrNull()?.message}",
                    )
                    currentUiState.copy(
                        isLoading = !isLoadingDone,
                        errorMessages = errorMessages,
                    )
                }
                Timber.tag("processTrendingList").e(repositoryResult.exceptionOrNull())
            }

            repositoryResult.isSuccess -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = !isLoadingDone,
                        giphyImageItems = repositoryResult.getOrNull() ?: emptyList(),
                    )
                }
                Timber.tag("processTrendingList").v("Processed ${repositoryResult.getOrNull()?.count() ?: 0} entries, isLoading = ${!isLoadingDone}")
            }
        }
    }
}
