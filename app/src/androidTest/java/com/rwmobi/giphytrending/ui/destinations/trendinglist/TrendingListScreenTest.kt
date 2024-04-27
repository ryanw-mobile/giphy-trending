/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.giphytrending.MainActivity
import com.rwmobi.giphytrending.data.repository.FakeUITestTrendingRepository
import com.rwmobi.giphytrending.data.repository.FakeUITestUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.ui.MainActivityTestRobot
import com.rwmobi.giphytrending.ui.test.SampleGiphyImageItemList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.inject.Inject

@kotlinx.coroutines.ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TrendingListScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var trendingRepository: TrendingRepository

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var trendingListTestRobot: TrendingListTestRobot
    private lateinit var fakeTrendingRepository: FakeUITestTrendingRepository
    private lateinit var fakeUserPreferencesRepository: FakeUITestUserPreferencesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        trendingListTestRobot = TrendingListTestRobot(composeTestRule)
        fakeTrendingRepository = trendingRepository as FakeUITestTrendingRepository
        fakeUserPreferencesRepository = (userPreferencesRepository as FakeUITestUserPreferencesRepository).apply {
            init(
                userPreferences = UserPreferences(
                    apiRequestLimit = 50,
                    rating = Rating.R,
                ),
            )
        }
    }

    @Test
    fun trendingListScreenJourneyTest() = runTest {
        fakeTrendingRepository.setTrendingResultForTest(
            trendingResult = Result.success(emptyList()),
        )

        with(mainActivityTestRobot) {
            assertTopAppBarIsVisible()
            assertNavigationItemsAreDisplayed()

            tapTrendingTab()
            assertTrendingTabIsSelected()
        }

        with(trendingListTestRobot) {
            checkNoDataScreenIsDisplayed()

            fakeTrendingRepository.setTrendingResultForTest(
                trendingResult = Result.success(SampleGiphyImageItemList.giphyImageItemList),
            )
            performPullToRefresh()
            checkTrendingListIsDisplayed()
            checkTrendingListContainsAllGiphyImageItems(giphyImageItemList = SampleGiphyImageItemList.giphyImageItemList)

            scrollToTrendingItem(index = SampleGiphyImageItemList.giphyImageItemList.lastIndex)
            assertTrendingItemIsDisplayed(giphyImageItem = SampleGiphyImageItemList.giphyImageItemList.last())
        }

        with(mainActivityTestRobot) {
            // Test scroll to top
            assertTrendingTabIsSelected()
            tapTrendingTab()
        }

        with(trendingListTestRobot) {
            assertTrendingItemIsDisplayed(giphyImageItem = SampleGiphyImageItemList.giphyImageItemList.first())

            // Check error message snackbar
            val exceptionMessage = "Testing Exception"
            fakeTrendingRepository.setTrendingResultForTest(Result.failure(IOException(exceptionMessage)))
            performPullToRefresh()
            assertSnackbarIsDisplayed(message = "Error getting data: $exceptionMessage")
            tapOK()
            assertSnackbarIsNotDisplayed(message = "Error getting data: $exceptionMessage")

            // Reload with only one item for easier testing
            val lastGiphyItem = SampleGiphyImageItemList.giphyImageItemList.last()
            fakeTrendingRepository.setTrendingResultForTest(
                trendingResult = Result.success(listOf(lastGiphyItem)),
            )
            performPullToRefresh()
            assertTrendingItemIsDisplayed(giphyImageItem = lastGiphyItem)

            checkGiphyImageItemButtonsLongClickToolTipAreDisplayed()
            checkOpenInBrowserButton(url = lastGiphyItem.webUrl)
            advanceUntilIdle()
            checkDownloadImageButton(imageUrl = lastGiphyItem.imageUrl)
            advanceUntilIdle()
            // This likely to have the clipboard floating dialog, so we put it to the end of test
            checkCopyImageLinkButton()
        }
    }
}
