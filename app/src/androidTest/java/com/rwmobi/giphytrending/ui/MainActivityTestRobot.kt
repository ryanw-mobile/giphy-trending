/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
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
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    fun tapTrendingTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(AppNavItem.TrendingList.titleResId))),
            ).performClick()
        }
    }

    fun tapSearchTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(AppNavItem.Search.titleResId))),
            ).performClick()
        }
    }

    fun tapSettingsTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(AppNavItem.Settings.titleResId))),
            ).performClick()
        }
    }

    fun assertTopAppBarIsVisible() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Image).and(hasContentDescription(value = activity.getString(R.string.app_name))),
            ).assertIsDisplayed()
        }
    }

    fun assertTopAppBarIsNotVisible() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Image).and(hasContentDescription(value = activity.getString(R.string.app_name))),
            ).assertDoesNotExist()
        }
    }

    fun assertNavigationBarIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_bar)).assertIsDisplayed()
        }
    }

    fun assertNavigationRailIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_rail)).assertIsDisplayed()
        }
    }

    fun assertNavigationItemsAreDisplayed() {
        with(composeTestRule) {
            for (navigationItem in AppNavItem.navigationBarItems) {
                onNode(
                    matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(navigationItem.titleResId))),
                ).assertIsDisplayed()
            }
        }
    }

    fun assertTrendingTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.trending))),
            ).assertIsSelected()

            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.settings))),
            ).assertIsNotSelected()
        }
    }

    fun assertSettingsTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.trending))),
            ).assertIsNotSelected()

            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.settings))),
            ).assertIsSelected()
        }
    }
}
