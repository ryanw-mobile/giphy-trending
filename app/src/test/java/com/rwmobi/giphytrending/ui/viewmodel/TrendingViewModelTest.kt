/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import coil.ImageLoader
import com.rwmobi.giphytrending.data.repository.FakeTrendingRepository
import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.test.testdata.SampleGifObjectList
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class TrendingViewModelTest {

    private lateinit var viewModel: TrendingViewModel
    private lateinit var fakeTrendingRepository: FakeTrendingRepository
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var mockImageLoader: ImageLoader

    private fun setupViewModel() {
        viewModel = TrendingViewModel(
            trendingRepository = fakeTrendingRepository,
            userPreferencesRepository = fakeUserPreferencesRepository,
            imageLoader = mockImageLoader,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Before
    fun setUp() {
        fakeTrendingRepository = FakeTrendingRepository()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        mockImageLoader = mockk(relaxed = true)
    }

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `starts with loading state true when initialised`() {
        setupViewModel()
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `sets UI state to empty and not loading when data fetch is empty`() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.success(emptyList()))

        setupViewModel()

        assertEquals(emptyList(), viewModel.uiState.value.gifObjects)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `updates UI state with error when data fetch fails`() {
        val errorMessage = "some error message"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.failure(Exception(errorMessage)))

        setupViewModel()

        assertTrue(viewModel.uiState.value.errorMessages.any { it.message.contains("Error getting data: $errorMessage") })
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `updates UI with new items when refresh succeeds`() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.success(emptyList()))
        setupViewModel()

        fakeTrendingRepository.setTrendingResultForTest(Result.success(SampleGifObjectList.gifObjects))
        viewModel.refresh()

        assertEquals(SampleGifObjectList.gifObjects, viewModel.uiState.value.gifObjects)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `displays error when user preferences are not fully configured during refresh`() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = null,
                rating = null,
            ),
        )
        setupViewModel()

        viewModel.refresh()

        assertTrue(viewModel.uiState.value.errorMessages.any { it.message.contains("Unable to access user preferences. Cannot refresh.") })
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `adds error message to UI state when user preference has errors`() = runTest {
        setupViewModel()
        val errorMessage = "Preference error"

        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        assertTrue(viewModel.uiState.value.errorMessages.any { it.message.contains(errorMessage) })
    }

    @Test
    fun `returns correct image loader instance`() {
        setupViewModel()
        val expectedImageLoader = mockImageLoader

        val imageLoader = viewModel.getImageLoader()

        assertSame(expectedImageLoader, imageLoader)
    }

    @Test
    fun `updates UI state with request scroll to top`() {
        setupViewModel()
        val expectedRequestScrollToTop = true

        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        assertEquals(expectedRequestScrollToTop, uiState.requestScrollToTop)
    }

    @Test
    fun `accumulates error messages on multiple failures`() = runTest {
        val errorMessage1 = "Test error 1"
        val errorMessage2 = "Test error 2"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        setupViewModel()

        fakeUserPreferencesRepository.emitError(Exception(errorMessage1))
        viewModel.refresh()
        fakeUserPreferencesRepository.emitError(Exception(errorMessage2))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertEquals(2, uiState.errorMessages.size)
        assertEquals(errorMessage1, uiState.errorMessages[0].message)
        assertEquals(errorMessage2, uiState.errorMessages[1].message)
    }

    @Test
    fun `does not accumulate duplicated error messages on multiple failures`() = runTest {
        val errorMessage1 = "Test error 1"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        setupViewModel()

        repeat(times = 2) {
            fakeUserPreferencesRepository.emitError(Exception(errorMessage1))
            viewModel.refresh()
        }

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(errorMessage1, uiState.errorMessages[0].message)
    }

    @Test
    fun `removes specified error message from UI state when error is shown`() = runTest {
        setupViewModel()
        fakeUserPreferencesRepository.emitError(Exception("some error message"))
        viewModel.refresh()
        val errorMessages = viewModel.uiState.value.errorMessages

        viewModel.errorShown(errorId = errorMessages.first().id)

        assertEquals(errorMessages.size - 1, viewModel.uiState.value.errorMessages.size)
    }
}
