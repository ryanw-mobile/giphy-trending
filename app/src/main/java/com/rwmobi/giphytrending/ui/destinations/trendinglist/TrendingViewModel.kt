/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
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
class TrendingViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageLoader: ImageLoader,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TrendingUIState> = MutableStateFlow(TrendingUIState(isLoading = true))
    var uiState = _uiState.asStateFlow()

    private var apiMaxEntries: Int? = null
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
                userPreferencesRepository.apiMaxEntries.collect {
                    apiMaxEntries = it
                    if (it != null && !firstRefreshDone) {
                        Timber.tag("refresh").v("got apimax entries, force refresh")
                        refresh()
                        firstRefreshDone = true
                    }
                }
            }
        }
    }

    fun refresh() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch(dispatcher) {
            apiMaxEntries?.let {
                Timber.tag("refresh").v("Requesting $it entries from the repository")
                processTrendingList(
                    repositoryResult = giphyRepository.reloadTrending(apiMaxEntries = it),
                    isLoadingDone = true,
                )
            } ?: _uiState.update { currentUiState ->
                val errorMessages = currentUiState.errorMessages + ErrorMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    message = "Unable to access user preferences. Cannot refresh.",
                )
                currentUiState.copy(
                    isLoading = false,
                    errorMessages = errorMessages,
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
                Timber.tag("processTrendingList").v("processed")
            }
        }
    }
}