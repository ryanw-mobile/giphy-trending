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
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
internal class TrendingViewModelTest : FreeSpec() {
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

    init {
        beforeTest {
            fakeGiphyRepository = FakeGiphyRepository()
            fakeUserPreferencesRepository = FakeUserPreferencesRepository()
            mockImageLoader = mockk(relaxed = true)
        }

        "ViewModel initialization" - {
            "should start with isLoading true" - {
                setupViewModel()

                viewModel.uiState.value.isLoading shouldBe true
            }

            "updates UI state correctly if repositories return success results" - {
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

            "updates UI state correctly if giphyRepository returns failure result" - {
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
        }

        "refresh" - {
            "updates UI state correctly if repositories return success results" - {
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

            "updates UI state correctly if userPreferences is not fully configured" - {
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
        }

        "User Preference Error Handling" - {
            "handles preference errors correctly" - {
                // Given
                setupViewModel()
                val errorMessage = "Preference error"

                // When
                fakeUserPreferencesRepository.emitError(Exception(errorMessage))

                // Then
                viewModel.uiState.value.errorMessages.any { it.message.contains(errorMessage) } shouldBe true
            }
        }

        "getImageLoader" - {
            "returns the provided ImageLoader object" - {
                // Given
                setupViewModel()
                val expectedImageLoader = mockImageLoader

                // When
                val imageLoader = viewModel.getImageLoader()

                // Then
                imageLoader shouldBeSameInstanceAs expectedImageLoader
            }
        }

        "errorShown" - {
            "removes the error message from the UIState" - {
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
    }
}
