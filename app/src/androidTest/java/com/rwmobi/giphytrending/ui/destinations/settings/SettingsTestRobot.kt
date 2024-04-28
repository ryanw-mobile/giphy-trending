/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.ui.test.GiphyTrendingTestRule
import com.rwmobi.giphytrending.ui.test.withRole
import junit.framework.TestCase.assertTrue

internal class SettingsTestRobot(
    private val composeTestRule: GiphyTrendingTestRule,
) {
    // Checks
    fun checkSettingsScreenIsDisplayed() {
        try {
            assertItemsToLoadSectionIsDisplayed()
            assertImageRatingSectionIsDisplayed()
            assertGitHubLinkIsDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SettingsTestRobotError")
            throw AssertionError("Expected settings screen sections are not displayed. ${e.message}", e)
        }
    }

    fun checkItemsToLoadSectionIsDisplayed() {
        try {
            assertItemsToLoadSectionIsDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SettingsTestRobotError")
            throw AssertionError("Expected Items To Load section is not displayed. ${e.message}", e)
        }
    }

    fun checkCurrentItemsToLoadValueIsCorrect(value: Float) {
        try {
            assertItemsToLoadSliderValue(value = value)
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SettingsTestRobotError")
            throw AssertionError("Expected items to load value $value is not displayed. ${e.message}", e)
        }
    }

    fun checkAllRatingOptionsAreSelectable() {
        try {
            for (rating in Rating.entries) {
                selectRatingRadioButton(rating = rating)
                assertRatingRadioButtonIsSelected(rating = rating)
            }
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("SettingsTestRobotError")
            throw AssertionError("Expected rating option selection behaviour is not displayed. ${e.message}", e)
        }
    }

    // Actions
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    fun swipeRightOnItemsToLoadSlider() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_slider)).performTouchInput {
                swipeRight()
            }
        }
    }

    fun performSwipeUp() {
        composeTestRule.onRoot().performTouchInput {
            swipeUp()
        }
    }

    private fun selectRatingRadioButton(rating: Rating) {
        with(composeTestRule) {
            onNode(
                matcher = withRole(role = Role.RadioButton) and hasText(text = rating.toString()),
            ).performClick()
        }
    }

    // Assertions
    private fun assertItemsToLoadSliderValue(value: Float) {
        with(composeTestRule) {
            val currentValue = onNode(
                matcher = hasContentDescription(value = activity.getString(R.string.content_description_slider)),
            ).fetchSemanticsNode().config[SemanticsProperties.ProgressBarRangeInfo].current
            assertTrue(currentValue == value)
        }
    }

    private fun assertItemsToLoadSectionIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.api_max_description)).assertIsDisplayed()
        }
    }

    private fun assertImageRatingSectionIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.rating)).assertIsDisplayed()
        }
    }

    private fun assertGitHubLinkIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.acknowledgement_sampleapp)).assertIsDisplayed()
        }
    }

    private fun assertRatingRadioButtonIsSelected(rating: Rating) {
        with(composeTestRule) {
            onNode(
                matcher = withRole(role = Role.RadioButton)
                    and hasText(text = rating.toString())
                    and hasStateDescription(value = activity.getString(R.string.selected)),
            ).assertExists()
        }
    }

    private fun assertRatingRadioButtonIsNotSelected(rating: Rating) {
        with(composeTestRule) {
            onNode(
                matcher = withRole(role = Role.RadioButton)
                    and hasText(text = rating.toString())
                    and hasStateDescription(value = activity.getString(R.string.not_selected)),
            ).assertExists()
        }
    }
}
