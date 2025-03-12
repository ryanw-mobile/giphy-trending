/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

import junit.framework.TestCase.assertFalse
import org.junit.Test
import kotlin.test.assertTrue

class UserPreferencesTest {

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun isFullyConfigured_ShouldReturnTrue_WhenAllFieldsAreNonNull() {
        val preferences = UserPreferences(apiRequestLimit = 100, rating = Rating.G)
        assertTrue(preferences.isFullyConfigured())
    }

    @Test
    fun isFullyConfigured_ShouldReturnFalse_WhenApiRequestLimitIsNull() {
        val preferences = UserPreferences(apiRequestLimit = null, rating = Rating.G)
        assertFalse(preferences.isFullyConfigured())
    }

    @Test
    fun isFullyConfigured_ShouldReturnFalse_WhenRatingIsNull() {
        val preferences = UserPreferences(apiRequestLimit = 100, rating = null)
        assertFalse(preferences.isFullyConfigured())
    }

    @Test
    fun isFullyConfigured_ShouldReturnFalse_WhenAllFieldsAreNull() {
        val preferences = UserPreferences(apiRequestLimit = null, rating = null)
        assertFalse(preferences.isFullyConfigured())
    }
}
