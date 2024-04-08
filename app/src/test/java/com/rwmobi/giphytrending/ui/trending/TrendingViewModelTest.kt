package com.rwmobi.giphytrending.ui.trending

import coil.ImageLoader
import com.rwmobi.giphytrending.data.repository.FakeGiphyRepository
import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.testdata.SampleGiphyImageItemList
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingViewModel
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
internal class TrendingViewModelTest : FreeSpec(
    {
        lateinit var viewModel: TrendingViewModel
        lateinit var fakeGiphyRepository: FakeGiphyRepository
        lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository

        fun mockImageLoader(): ImageLoader {
            // Provide your implementation or mock here
            return mockk(relaxed = true)
        }

        beforeTest {
            fakeGiphyRepository = FakeGiphyRepository()
            fakeUserPreferencesRepository = FakeUserPreferencesRepository()
            viewModel = TrendingViewModel(
                giphyRepository = fakeGiphyRepository,
                userPreferencesRepository = fakeUserPreferencesRepository,
                imageLoader = mockImageLoader(),
                dispatcher = UnconfinedTestDispatcher(),
            )
        }

        "ViewModel initialization" - {
            "should start with isLoading true" - {
                runTest {
                    viewModel.uiState.value.isLoading shouldBe true
                }
            }
        }

        "refresh" - {
            "updates UI state correctly if repository returns success" - {
                runTest {
                    // Given
                    fakeGiphyRepository.mockReloadTrendingResult = Result.success(SampleGiphyImageItemList.giphyImageItemList)

                    // When
                    viewModel.refresh()
                    // Coroutine launched in refresh() completes here

                    // Then
                    viewModel.uiState.value.giphyImageItems shouldBe SampleGiphyImageItemList.giphyImageItemList
                    viewModel.uiState.value.isLoading shouldBe false
                }
            }

            "updates UI state correctly if repository returns failure" - {
                runTest {
                    // Given
                    val exceptionMessage = "Network error"
                    fakeGiphyRepository.mockReloadTrendingResult = Result.failure(Exception(exceptionMessage))

                    // When
                    viewModel.refresh()

                    // Then
                    viewModel.uiState.value.errorMessages.any { it.message.contains(exceptionMessage) } shouldBe true
                    viewModel.uiState.value.isLoading shouldBe false
                }
            }
        }

        "User Preference Error Handling" - {
            "handles preference errors correctly" - {
                runTest {
                    // Given
                    val errorMessage = "Preference error"
                    fakeUserPreferencesRepository.emitError(Exception(errorMessage))

                    // Then
                    viewModel.uiState.value.errorMessages.any { it.message.contains(errorMessage) } shouldBe true
                }
            }
        }

        "API Max Entries Handling" - {
            "refreshes when apiMaxEntries is set and not yet refreshed" - {
                runTest {
                    // Given
                    val maxEntries = 100
                    fakeUserPreferencesRepository.apiMaxEntries.value = maxEntries
                    fakeGiphyRepository.mockReloadTrendingResult = Result.success(emptyList())

                    // Then
                    viewModel.uiState.value.isLoading shouldBe false // Assuming refresh sets isLoading to false after completion
                }
            }
        }
    },
)
