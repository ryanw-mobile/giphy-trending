/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import coil.ImageLoader
import com.rwmobi.giphytrending.data.repository.FakeGiphyRepository
import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.test.testdata.SampleGiphyImageItemList
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class TrendingViewModelTest {

    private lateinit var viewModel: TrendingViewModel
    private lateinit var fakeGiphyRepository: FakeGiphyRepository
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var mockImageLoader: ImageLoader

    private fun setupViewModel() {
        viewModel = TrendingViewModel(
            giphyRepository = fakeGiphyRepository,
            userPreferencesRepository = fakeUserPreferencesRepository,
            imageLoader = mockImageLoader,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Before
    fun setUp() {
        fakeGiphyRepository = FakeGiphyRepository()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        mockImageLoader = mockk(relaxed = true)
    }

    @Test
    fun `initialising ViewModel should start with isLoading true when initialised`() {
        setupViewModel()
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `initialising ViewModel should update UI state to empty and not loading on successful empty data fetch`() {
        // Given
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeGiphyRepository.fakeTrendingResult = Result.success(emptyList())

        // When
        setupViewModel()

        // Then
        viewModel.uiState.value.giphyImageItems shouldBe emptyList()
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `initialising ViewModel should update UI state with error message on data fetch failure`() {
        // Given
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeGiphyRepository.fakeTrendingResult = Result.failure(Exception("some error message"))

        // When
        setupViewModel()

        // Then
        viewModel.uiState.value.errorMessages.any { it.message.contains("Error getting data: some error message") } shouldBe true
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `refreshing data should update UI with new items from repository successfully`() {
        // Given
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeGiphyRepository.fakeTrendingResult = Result.success(emptyList())
        setupViewModel()

        // When
        fakeGiphyRepository.fakeTrendingResult = Result.success(SampleGiphyImageItemList.giphyImageItemList)
        viewModel.refresh()

        // Then
        viewModel.uiState.value.giphyImageItems shouldBe SampleGiphyImageItemList.giphyImageItemList
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `refreshing data should display error when user preferences are not fully configured`() {
        // Given
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = null,
                rating = null,
            ),
        )
        setupViewModel()

        // When
        viewModel.refresh()

        // Then
        viewModel.uiState.value.errorMessages.any { it.message.contains("Unable to access user preferences. Cannot refresh.") } shouldBe true
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `handling user preference errors should correctly add preference error messages to UIState`() = runTest {
        // Given
        setupViewModel()
        val errorMessage = "Preference error"

        // When
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        // Then
        viewModel.uiState.value.errorMessages.any { it.message.contains(errorMessage) } shouldBe true
    }

    @Test
    fun `retrieving ImageLoader should return the correct instance of ImageLoader`() {
        // Given
        setupViewModel()
        val expectedImageLoader = mockImageLoader

        // When
        val imageLoader = viewModel.getImageLoader()

        // Then
        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }

    @Test
    fun `handling request scroll to top should update the requestScrollToTop in UI state`() {
        // Given
        setupViewModel()
        val expectedRequestScrollToTop = true

        // When
        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        // Then
        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun `handling shown errors should remove the specified error message from UIState after being acknowledged`() = runTest {
        // Given
        setupViewModel()
        fakeUserPreferencesRepository.emitError(Exception("some error message"))
        viewModel.refresh()
        val errorMessages = viewModel.uiState.value.errorMessages

        // When
        viewModel.errorShown(errorId = errorMessages.first().id)

        // Then
        viewModel.uiState.value.errorMessages.size shouldBe errorMessages.size - 1
    }
}
