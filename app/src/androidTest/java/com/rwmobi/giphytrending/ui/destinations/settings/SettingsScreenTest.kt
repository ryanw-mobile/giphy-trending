/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.giphytrending.MainActivity
import com.rwmobi.giphytrending.data.repository.FakeUITestUserPreferencesRepository
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import com.rwmobi.giphytrending.ui.MainActivityTestRobot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.inject.Inject

@kotlinx.coroutines.ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var settingsTestRobot: SettingsTestRobot
    private lateinit var fakeUserPreferencesRepository: FakeUITestUserPreferencesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        settingsTestRobot = SettingsTestRobot(composeTestRule)
        fakeUserPreferencesRepository = (userPreferencesRepository as FakeUITestUserPreferencesRepository).apply {
            init(
                userPreferences = UserPreferences(
                    apiRequestLimit = 50,
                    rating = Rating.R,
                ),
            )
        }
    }

    @Test
    fun settingsScreenJourneyTest() = runTest {
        with(settingsTestRobot) {
            with(mainActivityTestRobot) {
                checkAppLayoutIsDisplayed()
                navigateToSettingsScreen()
                checkSettingsScreenIsDisplayed()
            }

            swipeRightOnItemsToLoadSlider()
            val newRating = fakeUserPreferencesRepository.userPreferences.value.apiRequestLimit!!.toFloat()
            checkCurrentItemsToLoadValueIsCorrect(value = newRating)

            checkAllRatingOptionsAreSelectable()

            // Error Snackbar
            with(mainActivityTestRobot) {
                val exceptionMessage = "Testing Exception"
                fakeUserPreferencesRepository.emitError(IOException(exceptionMessage))
                checkErrorSnackbarIsDisplayedAndDismissed(exceptionMessage = exceptionMessage)
            }

            // Currently not meaningful to test - until this screen becomes lengthy
            // Second top should scroll back to the top
            with(mainActivityTestRobot) {
                performSwipeUp()
                secondTapOnSettingsTab()
                checkItemsToLoadSectionIsDisplayed()
            }
        }
    }
}
