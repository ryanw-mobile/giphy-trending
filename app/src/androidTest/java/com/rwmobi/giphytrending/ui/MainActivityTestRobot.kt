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
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.utils.withRole

internal class MainActivityTestRobot(
    private val composeTestRule: giphyTrendingTestRule,
) {
    init {
        appBarIsVisible()
        trendingTabIsSelected()
        waitUntilTrendingListIsVisible()
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

    fun appBarIsVisible() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Image).and(hasContentDescription(value = activity.getString(R.string.app_name))),
            ).assertIsDisplayed()
        }
    }

    fun trendingTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.trending))),
            ).assertIsSelected()

            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.settings))),
            ).assertIsNotSelected()
        }
    }

    fun settingsTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.trending))),
            ).assertIsNotSelected()

            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.settings))),
            ).assertIsSelected()
        }
    }

    fun tapTrendingTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.trending))),
            ).performClick()
        }
    }

    fun tapSettingsTab() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.settings))),
            ).performClick()
        }
    }

    fun scrollToTrendingItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                label = activity.getString(R.string.content_description_trending_list),
                useUnmergedTree = true,
            ).performScrollToIndex(index)
        }
    }

    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }
}
