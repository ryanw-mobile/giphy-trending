/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class UserPreferencesTest {

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun isFullyConfigured_ShouldReturnTrue_WhenAllFieldsAreNonNull() {
        val preferences = UserPreferences(apiRequestLimit = 100, rating = Rating.G)
        preferences.isFullyConfigured() shouldBe true
    }

    @Test
    fun isFullyConfigured_ShouldReturnFalse_WhenApiRequestLimitIsNull() {
        val preferences = UserPreferences(apiRequestLimit = null, rating = Rating.G)
        preferences.isFullyConfigured() shouldBe false
    }

    @Test
    fun isFullyConfigured_ShouldReturnFalse_WhenRatingIsNull() {
        val preferences = UserPreferences(apiRequestLimit = 100, rating = null)
        preferences.isFullyConfigured() shouldBe false
    }

    @Test
    fun isFullyConfigured_ShouldReturnFalse_WhenAllFieldsAreNull() {
        val preferences = UserPreferences(apiRequestLimit = null, rating = null)
        preferences.isFullyConfigured() shouldBe false
    }
}
