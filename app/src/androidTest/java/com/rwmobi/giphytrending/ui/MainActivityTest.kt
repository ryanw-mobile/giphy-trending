/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.rwmobi.giphytrending.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
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
    val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
    }

    @After
    fun tearDown() {
        uiDevice.setOrientationNatural()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun appNavigationLayoutTest() {
        with(mainActivityTestRobot) {
            // Rotate to landscape
            uiDevice.setOrientationLeft()
            checkNavigationLayoutIsCorrect()

// Rotate to portrait
            uiDevice.setOrientationNatural()
            checkNavigationLayoutIsCorrect()
        }
    }
}
