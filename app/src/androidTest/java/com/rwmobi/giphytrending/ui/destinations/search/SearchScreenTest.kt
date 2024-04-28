/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.giphytrending.MainActivity
import com.rwmobi.giphytrending.data.repository.FakeUITestSearchRepository
import com.rwmobi.giphytrending.data.repository.FakeUITestUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.SearchRepository
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
class SearchScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var searchRepository: SearchRepository

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var searchTestRobot: SearchTestRobot
    private lateinit var giphyItemTestRobot: GiphyItemTestRobot
    private lateinit var fakeSearchRepository: FakeUITestSearchRepository
    private lateinit var fakeUserPreferencesRepository: FakeUITestUserPreferencesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        searchTestRobot = SearchTestRobot(composeTestRule)
        giphyItemTestRobot = GiphyItemTestRobot(composeTestRule)
        fakeSearchRepository = searchRepository as FakeUITestSearchRepository
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
    fun searchScreenJourneyTest() = runTest {
        with(searchTestRobot) {
            fakeSearchRepository.setSearchResultForTest(
                searchResult = Result.success(emptyList()),
            )
            with(mainActivityTestRobot) {
                checkAppLayoutIsDisplayed()
                navigateToSearchScreen()
            }
            checkEmptySearchScreenIsDisplayed()
            checkSearchBarCanInputAndClearKeyword()
            checkSearchEmptyKeywordShowsNoDataScreen()

            // Error Snackbar
            with(mainActivityTestRobot) {
                val exceptionMessage = "Testing Exception"
                fakeSearchRepository.setSearchResultForTest(Result.failure(IOException(exceptionMessage)))
                checkCanPerformSearchWithKeyword("any test keywords")
                checkErrorSnackbarIsDisplayedAndDismissed(exceptionMessage = "Error getting data: $exceptionMessage")
                checkCanClearSearchKeyword()
            }

            // Happy search flow
            with(giphyItemTestRobot) {
                fakeSearchRepository.setSearchResultForTest(
                    searchResult = Result.success(SampleGiphyImageItemList.giphyImageItemList),
                )
                checkCanPerformSearchWithKeyword("any test keywords")
                checkSearchResultsListIsDisplayed()
                for (index in 0..SampleGiphyImageItemList.giphyImageItemList.lastIndex) {
                    checkCanScrollToSearchResultItem(index = index)
                    checkGiphyItemIsDisplayed(giphyImageItem = SampleGiphyImageItemList.giphyImageItemList[index])
                }

                // Second top should scroll back to the top
                with(mainActivityTestRobot) {
                    secondTapOnSearchTab()
                }
                checkGiphyItemIsDisplayed(giphyImageItem = SampleGiphyImageItemList.giphyImageItemList.first())
            }

            // Reload with only one item for easier testing
            with(giphyItemTestRobot) {
                val lastGiphyItem = SampleGiphyImageItemList.giphyImageItemList.last()
                fakeSearchRepository.setSearchResultForTest(
                    searchResult = Result.success(listOf(lastGiphyItem)),
                )
                checkCanClearSearchKeyword()
                checkCanPerformSearchWithKeyword("any other test keywords")

                checkGiphyItemIsDisplayed(giphyImageItem = lastGiphyItem)

                checkGiphyImageItemButtonsLongClickToolTipAreDisplayed()
                checkOpenInBrowserButton(url = lastGiphyItem.webUrl)
                checkDownloadImageButton(imageUrl = lastGiphyItem.imageUrl)
                checkCopyImageLinkButton()
            }

            // Check the search keyword and result can survive navigation
            with(mainActivityTestRobot) {
                val lastSuccessfulSearchKeyword = "last search keyword"
                val lastSuccessfulSearchResult = listOf(SampleGiphyImageItemList.giphyImageItemList[1])

                navigateToTrendingScreen()
                fakeSearchRepository.setLastSuccessfulSearchKeywordForTest(lastSuccessfulSearchKeyword)
                fakeSearchRepository.setLastSuccessfulSearchResultsForTest(lastSuccessfulSearchResult)

                navigateToSearchScreen()
                checkSearchKeywordIsDisplayed(keyword = lastSuccessfulSearchKeyword)
                giphyItemTestRobot.checkGiphyItemIsDisplayed(giphyImageItem = SampleGiphyImageItemList.giphyImageItemList[1])
            }
        }
    }
}
