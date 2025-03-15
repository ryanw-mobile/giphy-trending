/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

data class TrendingUIEvent(
    val onRefresh: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onQueueDownloadSuccess: suspend () -> Unit,
    val onQueueDownloadFailed: suspend () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (String) -> Unit,
)
