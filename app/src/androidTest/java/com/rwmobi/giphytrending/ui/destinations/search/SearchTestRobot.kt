/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.test.GiphyTrendingTestRule

internal class SearchTestRobot(
    private val composeTestRule: GiphyTrendingTestRule,
) {
    // Checks
    fun checkEmptySearchScreenIsDisplayed() {
        try {
            assertSearchBarIsDisplayed()
            assertSearchBarIsNotFocused()
            assertSearchResultsIsNotDisplayed()
            assertNoDataScreenIsNotDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Expected empty search screen layout is not observed. ${e.message}", e)
        }
    }

    fun checkSearchBarCanInputAndClearKeyword() {
        try {
            val keyword = "some test keyword"
            tapOnSearchBar()
            assertSearchBarIsFocused()
            assertClearSearchKeywordButtonIsNotDisplayed()
            inputSearchKeyword(keyword = keyword)
            assertSearchKeywordIsDisplayed(keyword = keyword)
            assertClearSearchKeywordButtonIsDisplayed()
            tapClearSearchKeywordButton()
            assertSearchKeywordIsNotDisplayed(keyword = keyword)
            tapToDismissKeyboard()
            assertSearchBarIsNotFocused()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Expected search bar clear keyword behavior is not observed. ${e.message}", e)
        }
    }

    fun checkSearchEmptyKeywordShowsNoDataScreen() {
        try {
            tapOnSearchBar()
            assertSearchBarIsFocused()
            assertClearSearchKeywordButtonIsNotDisplayed() // means the input field is empty
            performSearch()
            assertSearchBarIsNotFocused()
            assertNoDataScreenIsDisplayed()
            assertSearchResultsIsNotDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Expected search empty keyword behavior is not observed. ${e.message}", e)
        }
    }

    fun checkCanClearSearchKeyword() {
        try {
            tapOnSearchBar()
            assertSearchBarIsFocused()
            tapClearSearchKeywordButton()
            tapToDismissKeyboard()
            assertSearchBarIsNotFocused()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Search bar clear keyword behavior is not performed. ${e.message}", e)
        }
    }

    fun checkCanPerformSearchWithKeyword(keyword: String) {
        try {
            tapOnSearchBar()
            assertSearchBarIsFocused()
            assertClearSearchKeywordButtonIsNotDisplayed() // means the input field is empty
            inputSearchKeyword(keyword = keyword)
            performSearch()
            assertSearchBarIsNotFocused()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Expected search keyword input behavior is not observed. ${e.message}", e)
        }
    }

    fun checkSearchKeywordIsDisplayed(keyword: String) {
        try {
            assertSearchKeywordIsDisplayed(keyword = keyword)
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Expected search keyword $keyword is not displayed. ${e.message}", e)
        }
    }

    fun checkSearchResultsListIsDisplayed() {
        try {
            assertSearchResultsIsDisplayed()
            assertNoDataScreenIsNotDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Expected search result list is not displayed. ${e.message}", e)
        }
    }

    fun checkCanScrollToSearchResultItem(index: Int) {
        try {
            scrollToSearchResultItem(index = index)
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SearchTestRobotError")
            throw AssertionError("Cannot scroll to the requested list index $index. ${e.message}", e)
        }
    }

    // Actions
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    private fun tapOnSearchBar() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_bar))
                .performClick()
        }
    }

    private fun inputSearchKeyword(keyword: String) {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_bar))
                .performTextInput(keyword)
        }
    }

    private fun tapClearSearchKeywordButton() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_clear_search_keyword)).performClick()
        }
    }

    private fun tapToDismissKeyboard() {
        with(composeTestRule) {
            onNodeWithTag(testTag = "layoutBox").performTouchInput {
                click()
            }
        }
    }

    private fun performSearch() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_bar))
                .performImeAction()
        }
    }

    private fun scrollToSearchResultItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                label = activity.getString(R.string.content_description_search_results),
                useUnmergedTree = true,
            ).performScrollToIndex(index)
        }
    }

    // Assertions
    private fun assertSearchBarIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_bar)).isDisplayed()
        }
    }

    private fun assertSearchBarIsFocused() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_bar)).assertIsFocused()
        }
    }

    private fun assertSearchBarIsNotFocused() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_bar)).assertIsNotFocused()
        }
    }

    private fun assertClearSearchKeywordButtonIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_clear_search_keyword)).assertIsDisplayed()
        }
    }

    private fun assertClearSearchKeywordButtonIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_clear_search_keyword)).assertDoesNotExist()
        }
    }

    private fun assertSearchKeywordIsDisplayed(keyword: String) {
        with(composeTestRule) {
            onNode(
                matcher = hasContentDescription(value = activity.getString(R.string.content_description_search_bar))
                    and hasText(keyword),
            ).assertIsDisplayed()
        }
    }

    private fun assertSearchKeywordIsNotDisplayed(keyword: String) {
        with(composeTestRule) {
            onNode(
                matcher = hasContentDescription(value = activity.getString(R.string.content_description_search_bar))
                    and hasText(keyword),
            ).assertDoesNotExist()
        }
    }

    private fun assertSearchResultsIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_results)).assertIsDisplayed()
        }
    }

    private fun assertSearchResultsIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_search_results)).assertDoesNotExist()
        }
    }

    private fun assertNoDataScreenIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.there_is_nothing_to_show_try_another_search)).assertIsDisplayed()
        }
    }

    private fun assertNoDataScreenIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.there_is_nothing_to_show_try_another_search)).assertDoesNotExist()
        }
    }
}
