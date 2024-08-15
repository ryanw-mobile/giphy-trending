/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import android.content.Context
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.ui.test.GiphyTrendingTestRule
import com.rwmobi.giphytrending.ui.test.withRole
import com.rwmobi.giphytrending.ui.utils.downloadImageUsingMediaStore
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify

internal class GiphyItemTestRobot(
    private val composeTestRule: GiphyTrendingTestRule,
) {
    // Checks
    fun checkGiphyItemIsDisplayed(gifObject: GifObject) {
        try {
            assertGiphyItemIsDisplayed(gifObject = gifObject)
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("GiphyItemTestRobotError")
            throw AssertionError("Expected Trending List item is not displayed. ${e.message}", e)
        }
    }

    fun checkGiphyImageItemButtonsLongClickToolTipAreDisplayed() {
        try {
            with(composeTestRule) {
                listOf(
                    R.string.content_description_copy_image_link,
                    R.string.content_description_download_image,
                    R.string.content_description_open_in_browser,
                ).forEach { tooltipResId ->
                    waitForIdle()
                    assertLongClickToolTipIsDisplayed(
                        contentDescription = activity.getString(tooltipResId),
                    )
                    waitForIdle()
                }
            }
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("GiphyItemTestRobotError")
            throw AssertionError("Expected button tool tip is not displayed. ${e.message}", e)
        }
    }

    fun checkCopyImageLinkButton() {
        try {
            with(composeTestRule) {
                tapCopyImageLinkButton()
                onNodeWithText(text = activity.getString(R.string.clipboard_copied)).assertIsDisplayed()
                tapOK()
            }
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("GiphyItemTestRobotError")
            throw AssertionError("Expected Copy Image Link button message is not displayed. ${e.message}", e)
        }
    }

    fun checkOpenInBrowserButton(url: String) {
        try {
            mockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
            every { any(Context::class).startBrowserActivity(any()) } just Runs

            tapOpenInBrowserButton()

            verify { any(Context::class).startBrowserActivity(url) }
            unmockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("GiphyItemTestRobotError")
            throw AssertionError("Expected Open In Browser button action is not triggered. ${e.message}", e)
        }
    }

    fun checkDownloadImageButton(imageUrl: String) {
        try {
            mockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
            every { any(Context::class).downloadImageUsingMediaStore(any()) } returns true

            tapDownloadImageButton()

            composeTestRule.waitForIdle()

            verify { any(Context::class).downloadImageUsingMediaStore(imageUrl) }
            unmockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("GiphyItemTestRobotError")
            throw AssertionError("Expected Download Image button action is not triggered. ${e.message}", e)
        }
    }

    // Actions
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    private fun tapCopyImageLinkButton() {
        with(composeTestRule) {
            onAllNodes(
                matcher = withRole(Role.Button)
                    and hasContentDescription(value = activity.getString(R.string.content_description_copy_image_link)),
            ).onFirst().performClick()
        }
    }

    private fun tapOpenInBrowserButton() {
        with(composeTestRule) {
            onAllNodes(
                matcher = withRole(Role.Button)
                    and hasContentDescription(value = activity.getString(R.string.content_description_open_in_browser)),
            ).onFirst().performClick()
        }
    }

    private fun tapDownloadImageButton() {
        with(composeTestRule) {
            onAllNodes(
                matcher = withRole(Role.Button)
                    and hasContentDescription(value = activity.getString(R.string.content_description_download_image)),
            ).onFirst().performClick()
        }
    }

    // Assertions
    private fun assertGiphyItemIsDisplayed(gifObject: GifObject) {
        with(composeTestRule) {
            onNodeWithText(text = gifObject.title).assertIsDisplayed()
            onNode(
                matcher = withRole(Role.Image) and hasContentDescription(value = gifObject.title),
            )
        }
    }

    private fun assertLongClickToolTipIsDisplayed(contentDescription: String) {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Button) and hasContentDescription(value = contentDescription),
            ).performTouchInput {
                longClick()
            }
            waitForIdle()

            // Attempt to perform the long-click with a retry mechanism
            repeat(3) {
                val isTooltipDisplayed = onNodeWithText(text = contentDescription).isDisplayed()
                if (isTooltipDisplayed) return
                waitForIdle()
            }

            throw AssertionError("Tooltip with contentDescription: $contentDescription not found.")
        }
    }

    private fun tapOK() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Button) and hasText(text = activity.getString(R.string.ok)),
            ).performClick()
        }
    }
}
