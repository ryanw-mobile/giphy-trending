/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import coil.ImageLoader
import com.rwmobi.giphytrending.data.repository.FakeTrendingRepository
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

    @Test
    fun `initialising ViewModel should start with isLoading true when initialised`() {
        setupViewModel()
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `initialising ViewModel should update UI state to empty and not loading on successful empty data fetch`() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.success(emptyList()))

        setupViewModel()

        viewModel.uiState.value.giphyImageItems shouldBe emptyList()
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `initialising ViewModel should update UI state with error message on data fetch failure`() {
        val errorMessage = "some error message"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.failure(Exception(errorMessage)))

        setupViewModel()

        viewModel.uiState.value.errorMessages.any { it.message.contains("Error getting data: $errorMessage") } shouldBe true
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `refreshing data should update UI with new items from repository successfully`() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.success(emptyList()))
        setupViewModel()

        fakeTrendingRepository.setTrendingResultForTest(Result.success(SampleGiphyImageItemList.giphyImageItemList))
        viewModel.refresh()

        viewModel.uiState.value.giphyImageItems shouldBe SampleGiphyImageItemList.giphyImageItemList
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `refreshing data should display error when user preferences are not fully configured`() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = null,
                rating = null,
            ),
        )
        setupViewModel()

        viewModel.refresh()

        viewModel.uiState.value.errorMessages.any { it.message.contains("Unable to access user preferences. Cannot refresh.") } shouldBe true
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun `handling user preference errors should correctly add preference error messages to UIState`() = runTest {
        setupViewModel()
        val errorMessage = "Preference error"

        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        viewModel.uiState.value.errorMessages.any { it.message.contains(errorMessage) } shouldBe true
    }

    @Test
    fun `retrieving ImageLoader should return the correct instance of ImageLoader`() {
        setupViewModel()
        val expectedImageLoader = mockImageLoader

        val imageLoader = viewModel.getImageLoader()

        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }

    @Test
    fun `handling request scroll to top should update the requestScrollToTop in UI state`() {
        setupViewModel()
        val expectedRequestScrollToTop = true

        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun `handling shown errors should remove the specified error message from UIState after being acknowledged`() = runTest {
        setupViewModel()
        fakeUserPreferencesRepository.emitError(Exception("some error message"))
        viewModel.refresh()
        val errorMessages = viewModel.uiState.value.errorMessages

        viewModel.errorShown(errorId = errorMessages.first().id)

        viewModel.uiState.value.errorMessages.size shouldBe errorMessages.size - 1
    }
}
