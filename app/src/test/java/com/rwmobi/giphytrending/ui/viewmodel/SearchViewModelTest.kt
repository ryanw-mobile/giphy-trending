/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.viewmodel

import coil.ImageLoader
import com.rwmobi.giphytrending.data.repository.FakeSearchRepository
import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.test.testdata.SampleGifObjectList
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel
    private lateinit var fakeSearchRepository: FakeSearchRepository
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var mockImageLoader: ImageLoader

    @Before
    fun setUp() {
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        fakeSearchRepository = FakeSearchRepository()
        mockImageLoader = mockk(relaxed = true)
        viewModel = SearchViewModel(
            searchRepository = fakeSearchRepository,
            userPreferencesRepository = fakeUserPreferencesRepository,
            imageLoader = mockImageLoader,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun fetchLastSuccessfulSearch_ShouldUpdateUIStateCorrectly_WhenDataIsAvailable() = runTest {
        val lastSuccessfulSearchKeyword = "last serach keyword"
        val lastSuccessfulSearchResult = SampleGifObjectList.gifObjects
        fakeSearchRepository.setLastSuccessfulSearchKeywordForTest(lastSuccessfulSearchKeyword)
        fakeSearchRepository.setLastSuccessfulSearchResultsForTest(lastSuccessfulSearchResult)

        viewModel.fetchLastSuccessfulSearch()

        with(viewModel.uiState.value) {
            isLoading shouldBe false
            keyword shouldBe lastSuccessfulSearchKeyword
            gifObjects shouldContainExactlyInAnyOrder lastSuccessfulSearchResult
        }
    }

    @Test
    fun fetchLastSuccessfulSearch_ShouldSetUIStateToDefault_WhenNoDataReturned() = runTest {
        viewModel.fetchLastSuccessfulSearch()

        with(viewModel.uiState.value) {
            isLoading shouldBe false
            keyword shouldBe ""
            gifObjects shouldBe null
        }
    }

    @Test
    fun updateKeyword_ShouldCorrectlyUpdateUIStateWithNewKeyword() = runTest {
        val keyword = "some search keyword"
        viewModel.updateKeyword(keyword)
        viewModel.uiState.value.keyword shouldBe keyword
    }

    @Test
    fun updateKeyword_ShouldClearKeyword_WhenNullInputReceived() = runTest {
        viewModel.updateKeyword(null)
        viewModel.uiState.value.keyword.isEmpty() shouldBe true
    }

    @Test
    fun updateKeyword_ShouldTrimKeywordToMaxLength_WhenExceedingLimit() = runTest {
        val maxLength = viewModel.uiState.value.keywordMaxLength
        val longKeyword = "x".repeat(100)

        viewModel.updateKeyword(longKeyword)

        viewModel.uiState.value.keyword.length shouldBe maxLength
    }

    @Test
    fun clearKeyword_ShouldResetKeywordInUIState() = runTest {
        viewModel.updateKeyword("test")
        viewModel.clearKeyword()
        viewModel.uiState.value.keyword.isEmpty() shouldBe true
    }

    @Test
    fun search_ShouldSucceedWithValidPreferences_AndUpdateUIState() = runTest {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeSearchRepository.setSearchResultForTest(Result.success(SampleGifObjectList.gifObjects))

        viewModel.updateKeyword("test")
        viewModel.search()

        viewModel.uiState.value.gifObjects shouldContainExactlyInAnyOrder SampleGifObjectList.gifObjects
    }

    @Test
    fun search_ShouldHandleError_WhenPreferencesNotSet() = runTest {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = null,
                rating = null,
            ),
        )

        viewModel.updateKeyword("test")
        viewModel.search()
        val uiState = viewModel.uiState.value

        with(uiState) {
            errorMessages.any { it.message.contains("Preferences are not fully set.") } shouldBe true
            isLoading shouldBe false
        }
    }

    @Test
    fun search_ShouldUpdateUIWithError_WhenSearchFails() = runTest {
        val errorMessage = "some error message"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeSearchRepository.setSearchResultForTest(Result.failure(RuntimeException(errorMessage)))

        viewModel.updateKeyword("test")
        viewModel.search()
        val uiState = viewModel.uiState.value

        with(uiState) {
            errorMessages.size shouldBe 1
            errorMessages.first().message shouldBe "Error getting data: $errorMessage"
            isLoading shouldBe false
        }
    }

    @Test
    fun getImageLoader_ShouldReturnCorrectImageLoaderInstance() {
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }

    @Test
    fun requestScrollToTop_ShouldUpdateRequestInUIState() {
        val expectedRequestScrollToTop = true

        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun searchError_ShouldUpdateUIStateAndSetLoadingFalse_WhenErrorOccurs() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val uiState = viewModel.uiState.value

        with(uiState) {
            errorMessages.size shouldBe 1
            errorMessages.first().message shouldBe errorMessage
            isLoading shouldBe false
        }
    }

    @Test
    fun errorMessages_ShouldAccumulateErrorMessages_OnMultipleFailures() = runTest {
        val errorMessage1 = "Test error 1"
        val errorMessage2 = "Test error 2"

        fakeUserPreferencesRepository.emitError(Exception(errorMessage1))
        fakeUserPreferencesRepository.emitError(Exception(errorMessage2))

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 2
        uiState.errorMessages[0].message shouldBe errorMessage1
        uiState.errorMessages[1].message shouldBe errorMessage2
    }

    @Test
    fun errorMessages_ShouldNotAccumulateDuplicatedErrorMessages_OnMultipleFailures() = runTest {
        val errorMessage = "Test error"

        repeat(times = 2) {
            fakeUserPreferencesRepository.emitError(Exception(errorMessage))
        }

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages[0].message shouldBe errorMessage
    }

    @Test
    fun errorShown_ShouldRemoveErrorMessageFromUIState_WhenCalled() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val errorId = viewModel.uiState.value.errorMessages.first().id
        viewModel.errorShown(errorId)

        val uiState = viewModel.uiState.value
        uiState.errorMessages shouldBe emptyList()
    }
}
