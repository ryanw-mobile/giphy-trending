/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.navigation.AppNavItem
import com.rwmobi.giphytrending.ui.test.GiphyTrendingTestRule
import com.rwmobi.giphytrending.ui.test.withRole

internal class MainActivityTestRobot(
    private val composeTestRule: GiphyTrendingTestRule,
) {
    fun checkAppLayoutIsDisplayed() {
        try {
            assertTopAppBarIsVisible()
            assertNavigationItemsAreDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected App layout is not displayed. ${e.message}")
        }
    }

    fun navigateToTrendingScreen() {
        try {
            tapTrendingTab()
            assertTrendingTabIsSelected()
            assertTopAppBarIsVisible()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected trending screen layout is not displayed. ${e.message}")
        }
    }

    fun navigateToSearchScreen() {
        try {
            tapSearchTab()
            assertSearchTabIsSelected()
            assertTopAppBarIsVisible()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected search screen layout is not displayed. ${e.message}")
        }
    }

    fun navigateToSettingsScreen() {
        try {
            tapSettingsTab()
            assertSettingsTabIsSelected()
            assertTopAppBarIsVisible()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected settings screen layout is not displayed. ${e.message}")
        }
    }

    fun secondTapOnTrendingTab() {
        try {
            assertTrendingTabIsSelected()
            navigateToTrendingScreen()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected second tap on trending screen behaviour is not observed. ${e.message}")
        }
    }

    fun secondTapOnSearchTab() {
        try {
            assertSearchTabIsSelected()
            navigateToSearchScreen()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected second tap on search screen behaviour is not observed. ${e.message}")
        }
    }

    fun secondTapOnSettingsTab() {
        try {
            assertSettingsTabIsSelected()
            navigateToSettingsScreen()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected second tap on settings screen behaviour is not observed. ${e.message}")
        }
    }

    fun checkErrorSnackbarIsDisplayedAndDismissed(exceptionMessage: String) {
        try {
            assertSnackbarIsDisplayed(message = exceptionMessage)
            tapOK()
            assertSnackbarIsNotDisplayed(message = exceptionMessage)
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected error snackbar behavior is not observed. ${e.message}")
        }
    }

    // Actions
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    private fun tapTrendingTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab) and hasContentDescription(value = activity.getString(AppNavItem.TrendingList.titleResId)),
            ).performClick()
        }
    }

    private fun tapSearchTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab) and hasContentDescription(value = activity.getString(AppNavItem.Search.titleResId)),
            ).performClick()
        }
    }

    private fun tapSettingsTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab) and hasContentDescription(value = activity.getString(AppNavItem.Settings.titleResId)),
            ).performClick()
        }
    }

    private fun tapOK() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Button) and hasText(text = activity.getString(R.string.ok)),
            ).performClick()
        }
    }

    // Assertions
    private fun assertTopAppBarIsVisible() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Image) and hasContentDescription(value = activity.getString(R.string.app_name)),
            ).assertIsDisplayed()
        }
    }

    private fun assertTopAppBarIsNotVisible() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Image) and hasContentDescription(value = activity.getString(R.string.app_name)),
            ).assertDoesNotExist()
        }
    }

    private fun assertNavigationBarIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_bar)).assertIsDisplayed()
        }
    }

    private fun assertNavigationRailIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_rail)).assertIsDisplayed()
        }
    }

    private fun assertNavigationItemsAreDisplayed() {
        with(composeTestRule) {
            for (navigationItem in AppNavItem.navigationBarItems) {
                onNode(
                    matcher = withRole(Role.Tab) and hasContentDescription(value = activity.getString(navigationItem.titleResId)),
                ).assertIsDisplayed()
            }
        }
    }

    private fun assertTrendingTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab) and hasContentDescription(value = activity.getString(AppNavItem.TrendingList.titleResId)),
            ).assertIsSelected()
        }
    }

    private fun assertSearchTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab) and hasContentDescription(value = activity.getString(AppNavItem.Search.titleResId)),
            ).assertIsSelected()
        }
    }

    private fun assertSettingsTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab) and hasContentDescription(value = activity.getString(AppNavItem.Settings.titleResId)),
            ).assertIsSelected()
        }
    }

    private fun assertSnackbarIsDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertIsDisplayed()
            onNode(
                matcher = withRole(Role.Button) and hasText(text = activity.getString(R.string.ok)),
            ).assertIsDisplayed()
        }
    }

    private fun assertSnackbarIsNotDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertDoesNotExist()
            onNode(
                matcher = withRole(Role.Button) and hasText(text = activity.getString(R.string.ok)),
            ).assertDoesNotExist()
        }
    }
}
