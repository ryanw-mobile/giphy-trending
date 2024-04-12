package com.rwmobi.giphytrending.domain.model

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class UserPreferencesTest : FreeSpec(
    {
        "UserPreferences" - {
            "isInitialised function" - {
                "should return true when all fields are non-null" {
                    val preferences = UserPreferences(apiRequestLimit = 100, rating = Rating.G)
                    preferences.isFullyConfigured() shouldBe true
                }

                "should return false when" - {
                    "apiMaxEntries is null" {
                        val preferences = UserPreferences(apiRequestLimit = null, rating = Rating.G)
                        preferences.isFullyConfigured() shouldBe false
                    }

                    "rating is null" {
                        val preferences = UserPreferences(apiRequestLimit = 100, rating = null)
                        preferences.isFullyConfigured() shouldBe false
                    }

                    "both fields are null" {
                        val preferences = UserPreferences(apiRequestLimit = null, rating = null)
                        preferences.isFullyConfigured() shouldBe false
                    }
                }
            }
        }
    },
)
