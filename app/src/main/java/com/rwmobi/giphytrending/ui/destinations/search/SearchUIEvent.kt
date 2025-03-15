/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

data class SearchUIEvent(
    val onFetchLastSuccessfulSearch: () -> Unit,
    val onUpdateKeyword: (keyword: String) -> Unit,
    val onClearKeyword: () -> Unit,
    val onSearch: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onQueueDownloadSuccess: suspend () -> Unit,
    val onQueueDownloadFailed: suspend () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (String) -> Unit,
)
