/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import com.rwmobi.giphytrending.domain.model.Rating

/**
 * UI actions for the Settings screen.
 */
data class SettingsUIActions(
    val onUpdateApiMaxEntries: (maxApiEntries: Int) -> Unit,
    val onUpdateRating: (rating: Rating) -> Unit,
    val onScrolledToTop: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
)

/**
 * One-time side effects for the Settings screen.
 */
sealed class SettingsEffect {
    data class ShowSnackbar(val message: String) : SettingsEffect()
}
