/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

import org.junit.Test
import kotlin.test.assertEquals

internal class RatingTest {

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun fromApiValue_ShouldReturnCorrectEnum_ForValidLowercaseInputs() {
        assertEquals(Rating.G, Rating.fromApiValue("g", Rating.PG))
        assertEquals(Rating.PG, Rating.fromApiValue("pg", Rating.G))
        assertEquals(Rating.PG_13, Rating.fromApiValue("pg-13", Rating.G))
        assertEquals(Rating.R, Rating.fromApiValue("r", Rating.G))
    }

    @Test
    fun fromApiValue_ShouldReturnCorrectEnum_ForValidUppercaseInputs() {
        assertEquals(Rating.G, Rating.fromApiValue("G", Rating.PG))
        assertEquals(Rating.PG, Rating.fromApiValue("PG", Rating.G))
        assertEquals(Rating.PG_13, Rating.fromApiValue("PG-13", Rating.G))
        assertEquals(Rating.R, Rating.fromApiValue("R", Rating.G))
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForUnknownRatingInputs() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue("x", defaultValue))
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForIncorrectlyFormattedRatingInputs() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue("pg13", defaultValue))
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForEmptyStringInput() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue("", defaultValue))
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForWhitespaceOnlyInput() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue(" ", defaultValue))
    }

    @Test
    fun fromApiValue_ShouldReturnDefaultValue_ForNullInput() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue(null, defaultValue))
    }
}
