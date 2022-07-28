package uk.ryanwong.giphytrending.ui.trending

import io.kotest.core.spec.style.FreeSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.giphytrending.data.repository.MockGiphyRepository
import uk.ryanwong.giphytrending.data.repository.MockUserPreferencesRepository

@ExperimentalCoroutinesApi
class TrendingViewModelTest : FreeSpec() {

    lateinit var trendingViewModel: TrendingViewModel
    lateinit var mockGiphyRepository: MockGiphyRepository
    lateinit var mockUserPreferencesRepository: MockUserPreferencesRepository
    private lateinit var dispatcher: TestDispatcher

    private fun setupViewModel() {
        mockUserPreferencesRepository = MockUserPreferencesRepository()
        dispatcher = UnconfinedTestDispatcher()

        trendingViewModel = TrendingViewModel(
            giphyRepository = mockGiphyRepository,
            userPreferencesRepository = mockUserPreferencesRepository,
            dispatcher = dispatcher
        )
    }

    init {
        "getTrendingUIState" - {
        }

        "setTrendingUIState" - {
        }

        "getTrendingList" - {
        }

        "setTrendingList" - {
        }

        "getListState" - {
        }

        "saveListState" - {
        }

        "refresh" - {
        }

        "onCleared" - {
        }

        "notifyErrorMessageDisplayed" - {
        }
    }
}