package com.rwmobi.giphytrending.domain.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class RatingTest {
    @Test
    fun `fromApiValue should return the correct enum for valid inputs when input is lowercase`() {
        Rating.fromApiValue("g", Rating.PG) shouldBe Rating.G
        Rating.fromApiValue("pg", Rating.G) shouldBe Rating.PG
        Rating.fromApiValue("pg-13", Rating.G) shouldBe Rating.PG_13
        Rating.fromApiValue("r", Rating.G) shouldBe Rating.R
    }

    @Test
    fun `fromApiValue should return the correct enum for valid inputs when input is uppercase`() {
        Rating.fromApiValue("G", Rating.PG) shouldBe Rating.G
        Rating.fromApiValue("PG", Rating.G) shouldBe Rating.PG
        Rating.fromApiValue("PG-13", Rating.G) shouldBe Rating.PG_13
        Rating.fromApiValue("R", Rating.G) shouldBe Rating.R
    }

    @Test
    fun `fromApiValue should return default value for invalid inputs when input is an unknown rating`() {
        val defaultValue = Rating.G
        Rating.fromApiValue("x", defaultValue) shouldBe defaultValue
    }

    @Test
    fun `fromApiValue should return default value for invalid inputs when input is an incorrectly formatted rating`() {
        val defaultValue = Rating.G
        Rating.fromApiValue("pg13", defaultValue) shouldBe defaultValue
    }

    @Test
    fun `fromApiValue should return default value for invalid inputs when input is an empty string`() {
        val defaultValue = Rating.G
        Rating.fromApiValue("", defaultValue) shouldBe defaultValue
    }

    @Test
    fun `fromApiValue should return default value for invalid inputs when input is only whitespace`() {
        val defaultValue = Rating.G
        Rating.fromApiValue(" ", defaultValue) shouldBe defaultValue
    }

    @Test
    fun `fromApiValue should handle null input by returning default value`() {
        val defaultValue = Rating.G
        Rating.fromApiValue(null, defaultValue) shouldBe defaultValue
    }
}
