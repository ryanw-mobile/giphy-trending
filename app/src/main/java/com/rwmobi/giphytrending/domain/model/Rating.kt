/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

import java.util.Locale

enum class Rating(val apiValue: String) {
    G(apiValue = "g"), // Contains images that are broadly accepted as appropriate and commonly witnessed by people in a public environment.
    PG(apiValue = "pg"), // Contains images that are commonly witnessed in a public environment, but not as broadly accepted as appropriate.
    PG_13(apiValue = "pg-13"), // Contains images that are typically not seen unless sought out, but still commonly witnessed.
    R(apiValue = "r"), // Contains images that are typically not seen unless sought out and could be considered alarming if witnessed.
    ;

    companion object {
        private val map = entries.associateBy(Rating::apiValue)

        fun fromApiValue(apiValue: String?, defaultValue: Rating): Rating {
            return map[apiValue?.lowercase(Locale.ENGLISH)] ?: defaultValue
        }
    }
}
