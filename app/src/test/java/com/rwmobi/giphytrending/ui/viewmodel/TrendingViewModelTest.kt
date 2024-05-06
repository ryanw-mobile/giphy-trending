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
import com.rwmobi.giphytrending.test.testdata.SampleGifObjectList
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

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun initialiseViewModel_ShouldStartWithIsLoadingTrue() {
        setupViewModel()
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun initialiseViewModel_ShouldSetUIStateToEmptyAndNotLoading_OnEmptyDataFetch() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.success(emptyList()))

        setupViewModel()

        viewModel.uiState.value.gifObjects shouldBe emptyList()
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun initialiseViewModel_ShouldUpdateUIStateWithError_OnDataFetchFailure() {
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
    fun refresh_ShouldUpdateUIWithNewItems_FromRepositorySuccessfully() {
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

        viewModel.uiState.value.gifObjects shouldBe SampleGifObjectList.gifObjects
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun refresh_ShouldDisplayError_WhenUserPreferencesNotFullyConfigured() {
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
    fun handleUserPreferenceErrors_ShouldAddErrorMessageToUIState() = runTest {
        setupViewModel()
        val errorMessage = "Preference error"

        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        viewModel.uiState.value.errorMessages.any { it.message.contains(errorMessage) } shouldBe true
    }

    @Test
    fun getImageLoader_ShouldReturnCorrectInstance() {
        setupViewModel()
        val expectedImageLoader = mockImageLoader

        val imageLoader = viewModel.getImageLoader()

        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }

    @Test
    fun requestScrollToTop_ShouldUpdateUIStateCorrectly() {
        setupViewModel()
        val expectedRequestScrollToTop = true

        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun errorShown_ShouldRemoveSpecifiedErrorMessageFromUIState() = runTest {
        setupViewModel()
        fakeUserPreferencesRepository.emitError(Exception("some error message"))
        viewModel.refresh()
        val errorMessages = viewModel.uiState.value.errorMessages

        viewModel.errorShown(errorId = errorMessages.first().id)

        viewModel.uiState.value.errorMessages.size shouldBe errorMessages.size - 1
    }
}
