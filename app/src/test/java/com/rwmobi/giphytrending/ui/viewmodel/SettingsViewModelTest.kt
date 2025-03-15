/*
 * Copyright (c) 2024-2025. Ryan Wong
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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `updates api request limit and sets isLoading to false when setApiRequestLimit is called`() {
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
    fun `updates rating and sets isLoading to false when setRating is called`() {
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
    fun `updates requestScrollToTop in UI state when requestScrollToTop is called`() {
        val expectedRequestScrollToTop = true

        settingsViewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = settingsViewModel.uiState.value

        assertEquals(expectedRequestScrollToTop, uiState.requestScrollToTop)
    }

    @Test
    fun `adds error message to UI state and sets isLoading to false when emitError is called`() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val uiState = settingsViewModel.uiState.value

        assertEquals(1, uiState.errorMessages.size)
        assertEquals(errorMessage, uiState.errorMessages.first().message)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `removes specified error message from UI state when errorShown is called`() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val errorId = settingsViewModel.uiState.value.errorMessages.first().id
        settingsViewModel.errorShown(errorId)

        val uiState = settingsViewModel.uiState.value
        assertTrue(uiState.errorMessages.isEmpty())
    }
}
