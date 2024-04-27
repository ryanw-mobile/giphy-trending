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
import com.rwmobi.giphytrending.ui.components.GiphyItemTestRobot
import com.rwmobi.giphytrending.ui.test.SampleGiphyImageItemList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
    private lateinit var giphyItemTestRobot: GiphyItemTestRobot
    private lateinit var fakeTrendingRepository: FakeUITestTrendingRepository
    private lateinit var fakeUserPreferencesRepository: FakeUITestUserPreferencesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        trendingListTestRobot = TrendingListTestRobot(composeTestRule)
        giphyItemTestRobot = GiphyItemTestRobot(composeTestRule)
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
        with(trendingListTestRobot) {
            fakeTrendingRepository.setTrendingResultForTest(
                trendingResult = Result.success(emptyList()),
            )

            with(mainActivityTestRobot) {
                checkAppLayoutIsDisplayed()
                navigateToTrendingScreen()
                checkNoDataScreenIsDisplayed()
            }

            // Pull to refresh with some data returned

            with(giphyItemTestRobot) {
                fakeTrendingRepository.setTrendingResultForTest(
                    trendingResult = Result.success(SampleGiphyImageItemList.giphyImageItemList),
                )
                performPullToRefresh()
                checkTrendingListIsDisplayed()
                for (index in 0..SampleGiphyImageItemList.giphyImageItemList.lastIndex) {
                    checkCanScrollToTrendingListItem(index = index)
                    checkGiphyItemIsDisplayed(giphyImageItem = SampleGiphyImageItemList.giphyImageItemList[index])
                }

                // Second top should scroll back to the top
                with(mainActivityTestRobot) {
                    secondTapOnTrendingTab()
                }
                checkGiphyItemIsDisplayed(giphyImageItem = SampleGiphyImageItemList.giphyImageItemList.first())
            }

            // Error Snackbar
            with(mainActivityTestRobot) {
                val exceptionMessage = "Testing Exception"
                fakeTrendingRepository.setTrendingResultForTest(Result.failure(IOException(exceptionMessage)))
                performPullToRefresh()
                checkErrorSnackbarIsDisplayedAndDismissed(exceptionMessage = "Error getting data: $exceptionMessage")
            }

            // Reload with only one item for easier testing
            with(giphyItemTestRobot) {
                val lastGiphyItem = SampleGiphyImageItemList.giphyImageItemList.last()
                fakeTrendingRepository.setTrendingResultForTest(
                    trendingResult = Result.success(listOf(lastGiphyItem)),
                )
                performPullToRefresh()

                checkGiphyItemIsDisplayed(giphyImageItem = lastGiphyItem)

                checkGiphyImageItemButtonsLongClickToolTipAreDisplayed()
                checkOpenInBrowserButton(url = lastGiphyItem.webUrl)
                checkDownloadImageButton(imageUrl = lastGiphyItem.imageUrl)
                // This likely to have the clipboard floating dialog, so we put it to the end of test
                checkCopyImageLinkButton()
            }
        }
    }
}
