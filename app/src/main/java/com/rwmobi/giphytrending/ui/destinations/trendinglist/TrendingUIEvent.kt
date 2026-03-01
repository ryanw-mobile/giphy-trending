/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

/**
 * UI actions for the Trending list screen.
 */
data class TrendingUIActions(
    val onRefresh: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onQueueDownloadSuccess: () -> Unit,
    val onQueueDownloadFailed: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
)

/**
 * One-time side effects for the Trending list screen.
 */
sealed class TrendingEffect {
    data class ShowSnackbar(val message: String) : TrendingEffect()
}
