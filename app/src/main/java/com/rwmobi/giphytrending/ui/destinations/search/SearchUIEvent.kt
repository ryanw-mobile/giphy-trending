/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

/**
 * UI actions for the Search screen.
 */
data class SearchUIActions(
    val onFetchLastSuccessfulSearch: () -> Unit,
    val onUpdateKeyword: (keyword: String) -> Unit,
    val onClearKeyword: () -> Unit,
    val onSearch: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onQueueDownloadSuccess: () -> Unit,
    val onQueueDownloadFailed: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
)

/**
 * One-time side effects for the Search screen.
 */
sealed class SearchEffect {
    data class ShowSnackbar(val message: String) : SearchEffect()
}
