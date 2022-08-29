package uk.ryanwong.giphytrending.ui.trending

import android.os.Parcel
import android.os.Parcelable
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.giphytrending.data.repository.MockGiphyRepository
import uk.ryanwong.giphytrending.data.repository.MockUserPreferencesRepository
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel

@ExperimentalCoroutinesApi
internal class TrendingViewModelTest : FreeSpec() {

    lateinit var trendingViewModel: TrendingViewModel
    lateinit var mockGiphyRepository: MockGiphyRepository
    lateinit var mockUserPreferencesRepository: MockUserPreferencesRepository
    private lateinit var dispatcher: TestDispatcher

    private class MockParcelable : Parcelable {
        override fun describeContents(): Int = 1
        override fun writeToParcel(p0: Parcel?, p1: Int) {}
    }

    private val mockGiphyImageItemDomainModelList = listOf(
        GiphyImageItemDomainModel(
            id = "some-id-1",
            previewUrl = "some-preview-url-1",
            imageUrl = "some-image-url-1",
            webUrl = "some-web-url-1",
            title = "some-title-1",
            type = "some-type-1",
            username = "some-user-name-1"
        ),
        GiphyImageItemDomainModel(
            id = "some-id-2",
            previewUrl = "some-preview-url-2",
            imageUrl = "some-image-url-2",
            webUrl = "some-web-url-2",
            title = "some-title-2",
            type = "some-type-2",
            username = "some-user-name-2"
        )
    )

    private fun setupViewModel() {
        mockUserPreferencesRepository = MockUserPreferencesRepository()
        mockGiphyRepository = MockGiphyRepository()
        dispatcher = UnconfinedTestDispatcher()

        trendingViewModel = TrendingViewModel(
            giphyRepository = mockGiphyRepository,
            userPreferencesRepository = mockUserPreferencesRepository,
            dispatcher = dispatcher
        )
    }

    init {
        "saveListState" - {
            "should store the Parcelable to listState correctly" {
                // 🔴 Given
                val someParcelable: Parcelable = MockParcelable()
                setupViewModel()

                // 🟡 When
                trendingViewModel.saveListState(listScrollingState = someParcelable)

                // 🟢 Then
                trendingViewModel.listState shouldBe someParcelable
            }

            "should be able to take null value" {
                // 🔴 Given
                setupViewModel()

                // 🟡 When
                trendingViewModel.saveListState(listScrollingState = null)

                // 🟢 Then
                trendingViewModel.listState shouldBe null
            }
        }

        "refresh" - {
            "should update trendingList correctlyif repository returned success" {
                // 🔴 Given
                setupViewModel()
                mockGiphyRepository.mockReloadTrendingResult =
                    Result.success(value = mockGiphyImageItemDomainModelList)

                // 🟡 When
                trendingViewModel.refresh()

                // 🟢 Then - not checking error message yet
                trendingViewModel.trendingList.first() shouldBe mockGiphyImageItemDomainModelList
            }

            "should set trendingUIState = ready if repository returned success" {
                // 🔴 Given
                setupViewModel()
                mockGiphyRepository.mockReloadTrendingResult =
                    Result.success(value = mockGiphyImageItemDomainModelList)

                // 🟡 When
                trendingViewModel.refresh()

                // 🟢 Then
                trendingViewModel.trendingUIState.first().shouldBeTypeOf<TrendingUIState.Ready>()
            }

            "should set trendingUIState = error if repository returned failure" {
                // 🔴 Given
                setupViewModel()
                mockGiphyRepository.mockReloadTrendingResult =
                    Result.failure(exception = Exception())

                // 🟡 When
                trendingViewModel.refresh()

                // 🟢 Then - not checking error message yet
                trendingViewModel.trendingUIState.first().shouldBeTypeOf<TrendingUIState.Error>()
            }
        }

        "notifyErrorMessageDisplayed" - {
            "should set trendingUIState = ready" {
                // 🔴 Given
                setupViewModel()
                mockGiphyRepository.mockReloadTrendingResult =
                    Result.failure(exception = Exception())
                trendingViewModel.refresh()

                // 🟡 When
                trendingViewModel.notifyErrorMessageDisplayed()

                // 🟢 Then
                trendingViewModel.trendingUIState.first().shouldBeTypeOf<TrendingUIState.Ready>()
            }
        }
    }
}
