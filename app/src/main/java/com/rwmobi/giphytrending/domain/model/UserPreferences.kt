/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

data class UserPreferences(
    val apiRequestLimit: Int?,
    val rating: Rating?,
) {
    fun isFullyConfigured(): Boolean {
        return apiRequestLimit != null && rating != null
    }
}
