package com.rwmobi.giphytrending.ui.trending

import android.os.Parcel
import android.os.Parcelable
import com.rwmobi.giphytrending.data.repository.MockGiphyRepository
import com.rwmobi.giphytrending.data.repository.MockUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingViewModel
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.giphytrending.domain.model.GiphyImageItem

@ExperimentalCoroutinesApi
internal class TrendingViewModelTest : FreeSpec() {

    lateinit var trendingViewModel: TrendingViewModel
    lateinit var mockGiphyRepository: MockGiphyRepository
    lateinit var mockUserPreferencesRepository: MockUserPreferencesRepository
    private lateinit var dispatcher: TestDispatcher

    private class MockParcelable : Parcelable {
        override fun describeContents(): Int = 1
        override fun writeToParcel(p0: Parcel, p1: Int) {}
    }

    private val mockGiphyImageItemList = listOf(
        GiphyImageItem(
            id = "some-id-1",
            previewUrl = "some-preview-url-1",
            imageUrl = "some-image-url-1",
            webUrl = "some-web-url-1",
            title = "some-title-1",
            type = "some-type-1",
            username = "some-user-name-1",
        ),
        GiphyImageItem(
            id = "some-id-2",
            previewUrl = "some-preview-url-2",
            imageUrl = "some-image-url-2",
            webUrl = "some-web-url-2",
            title = "some-title-2",
            type = "some-type-2",
            username = "some-user-name-2",
        ),
    )

    private fun setupViewModel() {
        mockUserPreferencesRepository = MockUserPreferencesRepository()
        mockGiphyRepository = MockGiphyRepository()
        dispatcher = UnconfinedTestDispatcher()

        trendingViewModel = TrendingViewModel(
            giphyRepository = mockGiphyRepository,
            userPreferencesRepository = mockUserPreferencesRepository,
            dispatcher = dispatcher,
        )
    }

    init {
        "refresh" - {
            "should update trendingList correctlyif repository returned success" {
                // 🔴 Given
                setupViewModel()
                mockGiphyRepository.mockReloadTrendingResult =
                    Result.success(value = mockGiphyImageItemList)

                // 🟡 When
                trendingViewModel.refresh()

                // 🟢 Then - not checking error message yet
                trendingViewModel.trendingList.first() shouldBe mockGiphyImageItemList
            }

            "should set trendingUIState = ready if repository returned success" {
                // 🔴 Given
                setupViewModel()
                mockGiphyRepository.mockReloadTrendingResult =
                    Result.success(value = mockGiphyImageItemList)

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
