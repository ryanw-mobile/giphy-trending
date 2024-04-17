/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.giphytrending.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@kotlinx.coroutines.ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mainActivityTestRobot: MainActivityTestRobot

    @Before
    fun setUp() {
        // Initialize Hilt for the test
        runTest {
            hiltRule.inject()
            mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        }
    }

    @Test
    fun appBasicJourneyTest() = runTest {
        with(mainActivityTestRobot) {
            printSemanticTree()
            for (index in 0..6) {
                scrollToTrendingItem(index = index)
            }
            tapSettingsTab()
            settingsTabIsSelected()
            printSemanticTree()
            tapTrendingTab()
            trendingTabIsSelected()
            printSemanticTree()
        }
    }
}
