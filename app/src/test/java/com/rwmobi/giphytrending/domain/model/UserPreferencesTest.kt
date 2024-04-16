/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class UserPreferencesTest {
    @Test
    fun `UserPreferences isInitialised should return true when all fields are non-null`() {
        val preferences = UserPreferences(apiRequestLimit = 100, rating = Rating.G)
        preferences.isFullyConfigured() shouldBe true
    }

    @Test
    fun `UserPreferences isInitialised should return false when apiMaxEntries is null`() {
        val preferences = UserPreferences(apiRequestLimit = null, rating = Rating.G)
        preferences.isFullyConfigured() shouldBe false
    }

    @Test
    fun `UserPreferences isInitialised should return false when rating is null`() {
        val preferences = UserPreferences(apiRequestLimit = 100, rating = null)
        preferences.isFullyConfigured() shouldBe false
    }

    @Test
    fun `UserPreferences isInitialised should return false when both fields are null`() {
        val preferences = UserPreferences(apiRequestLimit = null, rating = null)
        preferences.isFullyConfigured() shouldBe false
    }
}
