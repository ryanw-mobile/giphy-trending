/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import android.content.Context
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeDown
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
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

internal class TrendingListTestRobot(
    private val composeTestRule: GiphyTrendingTestRule,
) {
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    fun checkNoDataScreenIsDisplayed() {
        assertTrendingListIsNotDisplayed()
        assertNoDataScreenIsDisplayed()
    }

    fun checkTrendingListIsDisplayed() {
        assertNoDataScreenIsNotDisplayed()
        assertTrendingListIsDisplayed()
    }

    fun checkTrendingListContainsAllGiphyImageItems(giphyImageItemList: List<GiphyImageItem>) {
        for (index in 0..giphyImageItemList.lastIndex) {
            scrollToTrendingItem(index = index)
            assertTrendingItemIsDisplayed(giphyImageItem = giphyImageItemList[index])
        }
    }

    fun checkGiphyImageItemButtonsLongClickToolTipAreDisplayed() {
        with(composeTestRule) {
            assertLongClickToolTipIsDisplayed(
                contentDescription = activity.getString(R.string.content_description_copy_image_link),
            )
            assertLongClickToolTipIsDisplayed(
                contentDescription = activity.getString(R.string.content_description_download_image),
            )
            assertLongClickToolTipIsDisplayed(
                contentDescription = activity.getString(R.string.content_description_open_in_browser),
            )
        }
    }

    fun checkCopyImageLinkButton() {
        with(composeTestRule) {
            tapCopyImageLinkButton()
            onNodeWithText(text = activity.getString(R.string.clipboard_copied)).assertIsDisplayed()
        }
    }

    fun checkOpenInBrowserButton(url: String) {
        mockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
        every { any(Context::class).startBrowserActivity(any()) } just Runs

        tapOpenInBrowserButton()

        verify { any(Context::class).startBrowserActivity(url) }
        unmockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
    }

    fun checkDownloadImageButton(imageUrl: String) {
        mockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
        every { any(Context::class).downloadImageUsingMediaStore(any()) } returns true

        tapDownloadImageButton()

        verify { any(Context::class).downloadImageUsingMediaStore(imageUrl) }
        unmockkStatic("com.rwmobi.giphytrending.ui.utils.KotlinExtensionsKt")
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

    fun scrollToTrendingItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                label = activity.getString(R.string.content_description_trending_list),
                useUnmergedTree = true,
            ).performScrollToIndex(index)
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

    fun assertTrendingListIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_trending_list)).assertIsDisplayed()
        }
    }

    fun assertTrendingListIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_trending_list)).assertDoesNotExist()
        }
    }

    fun assertTrendingItemIsDisplayed(giphyImageItem: GiphyImageItem) {
        with(composeTestRule) {
            onNodeWithText(text = giphyImageItem.title).assertIsDisplayed()
            onNode(
                matcher = withRole(Role.Image).and(hasContentDescription(value = giphyImageItem.title)),
            )
        }
    }

    fun assertNoDataScreenIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.there_is_nothing_to_show_try_pull_to_reload)).assertIsDisplayed()
        }
    }

    fun assertNoDataScreenIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.there_is_nothing_to_show_try_pull_to_reload)).assertDoesNotExist()
        }
    }

    fun assertLongClickToolTipIsDisplayed(contentDescription: String) {
        with(composeTestRule) {
            onAllNodes(
                matcher = withRole(Role.Button).and(hasContentDescription(value = contentDescription)),
            ).onFirst().performTouchInput {
                longClick()
            }
            onNodeWithText(text = contentDescription).assertIsDisplayed()
            mainClock.advanceTimeBy(milliseconds = 5_000)
        }
    }

    fun tapOK() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).performClick()
        }
    }

    fun tapCopyImageLinkButton() {
        with(composeTestRule) {
            onAllNodes(
                matcher = withRole(Role.Button)
                    .and(hasContentDescription(value = activity.getString(R.string.content_description_copy_image_link))),
            ).onFirst().performClick()
        }
    }

    fun tapOpenInBrowserButton() {
        with(composeTestRule) {
            onAllNodes(
                matcher = withRole(Role.Button)
                    .and(hasContentDescription(value = activity.getString(R.string.content_description_open_in_browser))),
            ).onFirst().performClick()
        }
    }

    fun tapDownloadImageButton() {
        with(composeTestRule) {
            onAllNodes(
                matcher = withRole(Role.Button)
                    .and(hasContentDescription(value = activity.getString(R.string.content_description_download_image))),
            ).onFirst().performClick()
        }
    }

    fun assertSnackbarIsDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertIsDisplayed()
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).assertIsDisplayed()
        }
    }

    fun assertSnackbarIsNotDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertDoesNotExist()
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).assertDoesNotExist()
        }
    }
}
