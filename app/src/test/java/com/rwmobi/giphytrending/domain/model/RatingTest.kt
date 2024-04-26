/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class RatingTest {

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun fromApiValue_ShouldReturnCorrectEnum_ForValidLowercaseInputs() {
        Rating.fromApiValue("g", Rating.PG) shouldBe Rating.G
        Rating.fromApiValue("pg", Rating.G) shouldBe Rating.PG
        Rating.fromApiValue("pg-13", Rating.G) shouldBe Rating.PG_13
        Rating.fromApiValue("r", Rating.G) shouldBe Rating.R
    }

    @Test
    fun fromApiValue_ShouldReturnCorrectEnum_ForValidUppercaseInputs() {
        Rating.fromApiValue("G", Rating.PG) shouldBe Rating.G
        Rating.fromApiValue("PG", Rating.G) shouldBe Rating.PG
        Rating.fromApiValue("PG-13", Rating.G) shouldBe Rating.PG_13
        Rating.fromApiValue("R", Rating.G) shouldBe Rating.R
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForUnknownRatingInputs() {
        val defaultValue = Rating.G
        Rating.fromApiValue("x", defaultValue) shouldBe defaultValue
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForIncorrectlyFormattedRatingInputs() {
        val defaultValue = Rating.G
        Rating.fromApiValue("pg13", defaultValue) shouldBe defaultValue
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForEmptyStringInput() {
        val defaultValue = Rating.G
        Rating.fromApiValue("", defaultValue) shouldBe defaultValue
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForWhitespaceOnlyInput() {
        val defaultValue = Rating.G
        Rating.fromApiValue(" ", defaultValue) shouldBe defaultValue
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForNullInput() {
        val defaultValue = Rating.G
        Rating.fromApiValue(null, defaultValue) shouldBe defaultValue
    }
}
