package uk.ryanwong.giphytrending.ui.settings

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.giphytrending.data.repository.MockUserPreferencesRepository

@ExperimentalCoroutinesApi
class SettingsViewModelTest : FreeSpec() {

    lateinit var settingsViewModel: SettingsViewModel
    lateinit var mockUserPreferencesRepository: MockUserPreferencesRepository
    private lateinit var dispatcher: TestDispatcher

    private fun setupViewModel() {
        mockUserPreferencesRepository = MockUserPreferencesRepository()
        dispatcher = UnconfinedTestDispatcher()

        settingsViewModel = SettingsViewModel(
            userPreferencesRepository = mockUserPreferencesRepository,
            dispatcher = dispatcher
        )
    }

    init {
        "setApiMax" - {
            "should set the settingsUIState = ready if repository returns success" {
                // 🔴 Given
                setupViewModel()
                mockUserPreferencesRepository.mockSetApiMaxResponse = Result.success(Unit)

                // 🟡 When
                settingsViewModel.setApiMax(maxApiEntries = 100)

                // 🟢 Then
                settingsViewModel.settingsUIState.first().shouldBeTypeOf<SettingsUIState.Ready>()
            }

            "should set the settingsUIState = error if repository returns error" {
                // 🔴 Given
                setupViewModel()
                mockUserPreferencesRepository.mockSetApiMaxResponse =
                    Result.failure(exception = Exception())

                // 🟡 When
                settingsViewModel.setApiMax(maxApiEntries = 100)

                // 🟢 Then - TODO: should also assert error message format
                settingsViewModel.settingsUIState.first().shouldBeTypeOf<SettingsUIState.Error>()
            }
        }


        "getApiMax" - {
            "should set the settingsUIState = ready if repository returns success" {
                // 🔴 Given
                setupViewModel()
                mockUserPreferencesRepository.mockGetApiMaxResponse = Result.success(100)

                // 🟡 When
                settingsViewModel.getApiMax()

                // 🟢 Then
                settingsViewModel.settingsUIState.first().shouldBeTypeOf<SettingsUIState.Ready>()
            }

            "should set apiMaxEntriesProgress = adjusted value if repository returns success" {
                // 🔴 Given
                setupViewModel()
                mockUserPreferencesRepository.mockGetApiMaxResponse = Result.success(150)

                // 🟡 When
                settingsViewModel.getApiMax()

                // 🟢 Then
                settingsViewModel.apiMaxEntriesProgress.first() shouldBe 100
            }

            "should set apiMaxEntriesProgress = 0 if repository returns a value below the set threshold" {
                // 🔴 Given
                setupViewModel()
                mockUserPreferencesRepository.mockGetApiMaxResponse = Result.success(10)

                // 🟡 When
                settingsViewModel.getApiMax()

                // 🟢 Then
                settingsViewModel.apiMaxEntriesProgress.first() shouldBe 0
            }

            "should set the settingsUIState = error if repository returns error" {
                // 🔴 Given
                setupViewModel()
                mockUserPreferencesRepository.mockGetApiMaxResponse =
                    Result.failure(exception = Exception())

                // 🟡 When
                settingsViewModel.getApiMax()

                // 🟢 Then - not checking error message yet
                settingsViewModel.settingsUIState.first().shouldBeTypeOf<SettingsUIState.Error>()
            }
        }

        "translateMaxApiEntries" - {
            "should return the correct adjusted value in String" {
                // 🔴 Given
                setupViewModel()

                // 🟡 When
                val translatedValue = settingsViewModel.translateMaxApiEntries(value = 100)

                // 🟢 Then - not checking error message yet
                translatedValue shouldBe "150"
            }
        }
    }
}