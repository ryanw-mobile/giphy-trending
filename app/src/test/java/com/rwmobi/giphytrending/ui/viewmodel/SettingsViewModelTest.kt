/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class SettingsViewModelTest {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {
        testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        settingsViewModel = SettingsViewModel(
            userPreferencesRepository = fakeUserPreferencesRepository,
            dispatcher = testDispatcher,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setApiRequestLimit should update apiRequestLimit in UI state and set isLoading to false`() {
        // Given
        val expectedMaxApiEntries = 100
        fakeUserPreferencesRepository.init(
            UserPreferences(
                apiRequestLimit = 0,
                rating = Rating.G,
            ),
        )

        // When
        settingsViewModel.setApiRequestLimit(expectedMaxApiEntries)
        val uiState = settingsViewModel.uiState.value

        // Then
        uiState.apiRequestLimit shouldBe expectedMaxApiEntries
        uiState.isLoading shouldBe false
    }

    @Test
    fun `setRating should update the rating in UI state and set isLoading to false`() {
        // Given
        val expectedRating = Rating.R
        fakeUserPreferencesRepository.init(
            UserPreferences(
                apiRequestLimit = 0,
                rating = Rating.G,
            ),
        )

        // When
        settingsViewModel.setRating(expectedRating)
        val uiState = settingsViewModel.uiState.value

        // Then
        uiState.rating shouldBe expectedRating
        uiState.isLoading shouldBe false
    }

    @Test
    fun `requestScrollToTop should update the requestScrollToTop in UI state`() {
        // Given
        val expectedRequestScrollToTop = true

        // When
        settingsViewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = settingsViewModel.uiState.value

        // Then
        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun `should add the emitted error to UI state and set isLoading to false`() = runTest {
        // Given
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        // Wait for collection
        runBlocking {
            delay(100) // Small delay to ensure flow collects the error
        }

        // When
        val uiState = settingsViewModel.uiState.value

        // Then
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages.first().message shouldBe errorMessage
        uiState.isLoading shouldBe false
    }

    @Test
    fun `errorShown should remove the specified error message from UI state`() = runTest {
        // Given
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        // Wait for collection
        runBlocking {
            delay(100) // Small delay to ensure flow collects the error
        }

        // When
        val errorId = settingsViewModel.uiState.value.errorMessages.first().id
        settingsViewModel.errorShown(errorId)

        // Then
        val uiState = settingsViewModel.uiState.value
        uiState.errorMessages shouldBe emptyList()
    }
}
