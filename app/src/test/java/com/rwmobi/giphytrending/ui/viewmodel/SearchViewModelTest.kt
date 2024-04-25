package com.rwmobi.giphytrending.ui.viewmodel

import coil.ImageLoader
import com.rwmobi.giphytrending.data.repository.FakeSearchRepository
import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.test.testdata.SampleGiphyImageItemList
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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

    @Test
    fun `updateKeyword should update keyword correctly`() = runTest {
        val keyword = "some search keyword"
        viewModel.updateKeyword(keyword)
        assert(viewModel.uiState.value.keyword == keyword)
    }

    @Test
    fun `updateKeyword should handle null input by clearing keyword`() = runTest {
        viewModel.updateKeyword(null)
        assert(viewModel.uiState.value.keyword.isEmpty())
    }

    @Test
    fun `updateKeyword should trim the keyword to the max length`() = runTest {
        val maxLength = viewModel.uiState.value.keywordMaxLength
        val longKeyword = "x".repeat(100)

        viewModel.updateKeyword(longKeyword)

        viewModel.uiState.value.keyword.length shouldBe maxLength
    }

    @Test
    fun `clearKeyword should reset keyword state`() = runTest {
        viewModel.updateKeyword("test")
        viewModel.clearKeyword()
        assert(viewModel.uiState.value.keyword.isEmpty())
    }

    @Test
    fun `search should process correctly with valid preferences`() = runTest {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeSearchRepository.setSearchResultForTest(Result.success(SampleGiphyImageItemList.giphyImageItemList))

        viewModel.updateKeyword("test")
        viewModel.search()

        viewModel.uiState.value.giphyImageItems shouldContainExactlyInAnyOrder SampleGiphyImageItemList.giphyImageItemList
    }

    @Test
    fun `search should handle error when preferences are not set`() = runTest {
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
    fun `search should update UI on search failure`() = runTest {
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
    fun `retrieving ImageLoader should return the correct instance of ImageLoader`() {
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }

    @Test
    fun `requestScrollToTop should update the requestScrollToTop in UI state`() {
        val expectedRequestScrollToTop = true

        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun `should add the emitted error to UI state and set isLoading to false`() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        runBlocking {
            delay(100) // Small delay to ensure flow collects the error
        }
        val uiState = viewModel.uiState.value

        with(uiState) {
            errorMessages.size shouldBe 1
            errorMessages.first().message shouldBe errorMessage
            isLoading shouldBe false
        }
    }

    @Test
    fun `errorShown should remove the specified error message from UI state`() = runTest {
        val errorMessage = "Test error"
        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        runBlocking {
            delay(100) // Small delay to ensure flow collects the error
        }
        val errorId = viewModel.uiState.value.errorMessages.first().id
        viewModel.errorShown(errorId)

        val uiState = viewModel.uiState.value
        uiState.errorMessages shouldBe emptyList()
    }
}
