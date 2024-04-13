package com.rwmobi.giphytrending.ui.settings

import com.rwmobi.giphytrending.data.repository.FakeUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.ui.viewmodel.SettingsViewModel
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
internal class SettingsViewModelTest : FreeSpec(
    {
        lateinit var settingsViewModel: SettingsViewModel
        lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
        lateinit var testDispatcher: TestDispatcher

        beforeTest {
            testDispatcher = UnconfinedTestDispatcher()
            Dispatchers.setMain(testDispatcher)

            fakeUserPreferencesRepository = FakeUserPreferencesRepository()
            settingsViewModel = SettingsViewModel(
                userPreferencesRepository = fakeUserPreferencesRepository,
                dispatcher = testDispatcher,
            )
        }

        afterTest {
            Dispatchers.resetMain()
        }

        "setApiMax updates UI state correctly" - {
            "when setApiMax is called, it updates apiMaxEntries in the UI state" {
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
        }

        "Error handling" - {
            "when an error is emitted, it is added to the UI state" {
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

            "errorShown removes the error message from the UI state" {
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
    },
)
