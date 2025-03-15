/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

import org.junit.Test
import kotlin.test.assertEquals

internal class RatingTest {

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `returns correct enum when given valid lowercase input`() {
        assertEquals(Rating.G, Rating.fromApiValue("g", Rating.PG))
        assertEquals(Rating.PG, Rating.fromApiValue("pg", Rating.G))
        assertEquals(Rating.PG_13, Rating.fromApiValue("pg-13", Rating.G))
        assertEquals(Rating.R, Rating.fromApiValue("r", Rating.G))
    }

    @Test
    fun `returns correct enum when given valid uppercase input`() {
        assertEquals(Rating.G, Rating.fromApiValue("G", Rating.PG))
        assertEquals(Rating.PG, Rating.fromApiValue("PG", Rating.G))
        assertEquals(Rating.PG_13, Rating.fromApiValue("PG-13", Rating.G))
        assertEquals(Rating.R, Rating.fromApiValue("R", Rating.G))
    }

    @Test
    fun `returns default value when given unknown rating input`() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue("x", defaultValue))
    }

    @Test
    fun `returns default value when given incorrectly formatted rating input`() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue("pg13", defaultValue))
    }

    @Test
    fun `returns default value when given empty string input`() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue("", defaultValue))
    }

    @Test
    fun `returns default value when given whitespace only input`() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue(" ", defaultValue))
    }

    @Test
    fun `returns default value when given null input`() {
        val defaultValue = Rating.G
        assertEquals(defaultValue, Rating.fromApiValue(null, defaultValue))
    }
}
