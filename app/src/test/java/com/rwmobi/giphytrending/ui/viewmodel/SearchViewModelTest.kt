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
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

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
            assertFalse(isLoading)
            assertEquals(lastSuccessfulSearchKeyword, keyword)
            assertContentEquals(lastSuccessfulSearchResult, gifObjects)
        }
    }

    @Test
    fun fetchLastSuccessfulSearch_ShouldSetUIStateToDefault_WhenNoDataReturned() = runTest {
        viewModel.fetchLastSuccessfulSearch()

        with(viewModel.uiState.value) {
            assertFalse(isLoading)
            assertTrue(keyword.isEmpty())
            assertNull(gifObjects)
        }
    }

    @Test
    fun updateKeyword_ShouldCorrectlyUpdateUIStateWithNewKeyword() = runTest {
        val keyword = "some search keyword"
        viewModel.updateKeyword(keyword)
        assertEquals(keyword, viewModel.uiState.value.keyword)
    }

    @Test
    fun updateKeyword_ShouldClearKeyword_WhenNullInputReceived() = runTest {
        viewModel.updateKeyword(null)
        assertTrue(viewModel.uiState.value.keyword.isEmpty())
    }

    @Test
    fun updateKeyword_ShouldTrimKeywordToMaxLength_WhenExceedingLimit() = runTest {
        val maxLength = viewModel.uiState.value.keywordMaxLength
        val longKeyword = "x".repeat(100)
        viewModel.updateKeyword(longKeyword)
        assertEquals(maxLength, viewModel.uiState.value.keyword.length)
    }

    @Test
    fun clearKeyword_ShouldResetKeywordInUIState() = runTest {
        viewModel.updateKeyword("test")
        viewModel.clearKeyword()
        assertTrue(viewModel.uiState.value.keyword.isEmpty())
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

        assertContentEquals(SampleGifObjectList.gifObjects, viewModel.uiState.value.gifObjects)
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
            assertTrue(errorMessages.any { it.message.contains("Preferences are not fully set.") })
            assertFalse(isLoading)
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
            assertEquals(1, errorMessages.size)
            assertEquals("Error getting data: $errorMessage", errorMessages.first().message)
            assertFalse(isLoading)
        }
    }

    @Test
    fun getImageLoader_ShouldReturnCorrectImageLoaderInstance() {
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        assertSame(expectedImageLoader, imageLoader)
    }

    @Test
    fun requestScrollToTop_ShouldUpdateRequestInUIState() {
        val expectedRequestScrollToTop = true

        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        assertEquals(expectedRequestScrollToTop, uiState.requestScrollToTop)
    }

    @Test
    fun searchError_ShouldUpdateUIStateAndSetLoadingFalse_WhenErrorOccurs() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val uiState = viewModel.uiState.value

        with(uiState) {
            assertEquals(1, errorMessages.size)
            assertEquals(errorMessage, errorMessages.first().message)
            assertFalse(isLoading)
        }
    }

    @Test
    fun errorMessages_ShouldAccumulateErrorMessages_OnMultipleFailures() = runTest {
        val errorMessage1 = "Test error 1"
        val errorMessage2 = "Test error 2"

        fakeUserPreferencesRepository.emitError(Exception(errorMessage1))
        fakeUserPreferencesRepository.emitError(Exception(errorMessage2))

        val uiState = viewModel.uiState.value

        with(uiState) {
            assertEquals(2, errorMessages.size)
            assertEquals(errorMessage1, errorMessages[0].message)
            assertEquals(errorMessage2, errorMessages[1].message)
        }
    }

    @Test
    fun errorMessages_ShouldNotAccumulateDuplicatedErrorMessages_OnMultipleFailures() = runTest {
        val errorMessage = "Test error"

        repeat(times = 2) {
            fakeUserPreferencesRepository.emitError(Exception(errorMessage))
        }

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(errorMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun errorShown_ShouldRemoveErrorMessageFromUIState_WhenCalled() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        val errorId = viewModel.uiState.value.errorMessages.first().id
        viewModel.errorShown(errorId)

        val uiState = viewModel.uiState.value
        assertTrue(uiState.errorMessages.isEmpty())
    }
}
