/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class SettingsViewModelTest {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {
        testDispatcher = UnconfinedTestDispatcher()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        settingsViewModel = SettingsViewModel(
            userPreferencesRepository = fakeUserPreferencesRepository,
            dispatcher = testDispatcher,
        )
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun setApiRequestLimit_ShouldUpdateApiRequestLimitAndSetIsLoadingToFalse() {
        val expectedMaxApiEntries = 100
        fakeUserPreferencesRepository.init(
            UserPreferences(
                apiRequestLimit = 0,
                rating = Rating.G,
            ),
        )

        settingsViewModel.setApiRequestLimit(expectedMaxApiEntries)
        val uiState = settingsViewModel.uiState.value

        assertEquals(expectedMaxApiEntries, uiState.apiRequestLimit)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun setRating_ShouldUpdateRatingInUIStateAndSetIsLoadingToFalse() {
        val expectedRating = Rating.R
        fakeUserPreferencesRepository.init(
            UserPreferences(
                apiRequestLimit = 0,
                rating = Rating.G,
            ),
        )

        settingsViewModel.setRating(expectedRating)
        val uiState = settingsViewModel.uiState.value

        assertEquals(expectedRating, uiState.rating)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun requestScrollToTop_ShouldUpdateRequestInUIStateCorrectly() {
        val expectedRequestScrollToTop = true

        settingsViewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = settingsViewModel.uiState.value

        assertEquals(expectedRequestScrollToTop, uiState.requestScrollToTop)
    }

    @Test
    fun emitError_ShouldAddErrorMessageToUIStateAndSetIsLoadingToFalse() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val uiState = settingsViewModel.uiState.value

        assertEquals(1, uiState.errorMessages.size)
        assertEquals(errorMessage, uiState.errorMessages.first().message)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun errorShown_ShouldRemoveSpecifiedErrorMessageFromUIState() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val errorId = settingsViewModel.uiState.value.errorMessages.first().id
        settingsViewModel.errorShown(errorId)

        val uiState = settingsViewModel.uiState.value
        assertTrue(uiState.errorMessages.isEmpty())
    }
}
