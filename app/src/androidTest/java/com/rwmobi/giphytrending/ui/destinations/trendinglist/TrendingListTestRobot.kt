/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeDown
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.test.GiphyTrendingTestRule

internal class TrendingListTestRobot(
    private val composeTestRule: GiphyTrendingTestRule,
) {
    // Checks
    fun checkNoDataScreenIsDisplayed() {
        try {
            assertTrendingListIsNotDisplayed()
            assertNoDataScreenIsDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("TrendingListTestRobotError")
            throw AssertionError("Expected No Data screen is not displayed. ${e.message}", e)
        }
    }

    fun checkTrendingListIsDisplayed() {
        try {
            assertNoDataScreenIsNotDisplayed()
            assertTrendingListIsDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("TrendingListTestRobotError")
            throw AssertionError("Expected Trending List is not displayed. ${e.message}", e)
        }
    }

    fun checkCanScrollToTrendingListItem(index: Int) {
        try {
            scrollToTrendingListItem(index = index)
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("TrendingListTestRobotError")
            throw AssertionError("Cannot scroll to the requested list index $index. ${e.message}", e)
        }
    }

    // Actions
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    fun waitUntilTrendingListIsVisible() {
        with(composeTestRule) {
            waitUntil(timeoutMillis = 1_000) {
                onNodeWithContentDescription(
                    label = activity.getString(R.string.content_description_trending_list),
                    useUnmergedTree = true,
                ).isDisplayed()
            }
        }
    }

    fun performPullToRefresh() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_pull_to_refresh))
                .performTouchInput {
                    swipeDown(
                        startY = 0f,
                        endY = 500f,
                        durationMillis = 1_000,
                    )
                }

            // UI has intentional delay as loading effect
            mainClock.advanceTimeBy(milliseconds = 2_000)
        }
    }

    private fun scrollToTrendingListItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                label = activity.getString(R.string.content_description_trending_list),
                useUnmergedTree = true,
            ).performScrollToIndex(index)
        }
    }

    // Assertions
    private fun assertTrendingListIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_trending_list)).assertIsDisplayed()
        }
    }

    private fun assertTrendingListIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_trending_list)).assertDoesNotExist()
        }
    }

    private fun assertNoDataScreenIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.there_is_nothing_to_show_try_pull_to_reload)).assertIsDisplayed()
        }
    }

    private fun assertNoDataScreenIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.there_is_nothing_to_show_try_pull_to_reload)).assertDoesNotExist()
        }
    }
}
