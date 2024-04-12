package com.rwmobi.giphytrending.domain.model

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class RatingTest : FreeSpec(
    {
        "fromApiValue" - {
            "should return the correct enum for valid inputs" - {
                "when input is lowercase" {
                    Rating.fromApiValue("g", Rating.PG) shouldBe Rating.G
                    Rating.fromApiValue("pg", Rating.G) shouldBe Rating.PG
                    Rating.fromApiValue("pg-13", Rating.G) shouldBe Rating.PG_13
                    Rating.fromApiValue("r", Rating.G) shouldBe Rating.R
                }
                "when input is uppercase" {
                    Rating.fromApiValue("G", Rating.PG) shouldBe Rating.G
                    Rating.fromApiValue("PG", Rating.G) shouldBe Rating.PG
                    Rating.fromApiValue("PG-13", Rating.G) shouldBe Rating.PG_13
                    Rating.fromApiValue("R", Rating.G) shouldBe Rating.R
                }
            }

            "should return default value for invalid inputs" - {
                val defaultValue = Rating.G
                "when input is an unknown rating" {
                    Rating.fromApiValue("x", defaultValue) shouldBe defaultValue
                }
                "when input is an incorrectly formatted rating" {
                    Rating.fromApiValue("pg13", defaultValue) shouldBe defaultValue
                }
                "when input is an empty string" {
                    Rating.fromApiValue("", defaultValue) shouldBe defaultValue
                }
                "when input is only whitespace" {
                    Rating.fromApiValue(" ", defaultValue) shouldBe defaultValue
                }
            }

            "should handle null input by returning default value" {
                val defaultValue = Rating.G
                Rating.fromApiValue(null, defaultValue) shouldBe defaultValue
            }
        }
    },
)
