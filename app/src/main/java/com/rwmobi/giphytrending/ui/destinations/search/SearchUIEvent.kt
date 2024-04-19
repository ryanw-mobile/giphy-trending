/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

data class SearchUIEvent(
    val onUpdateKeyword: (keyword: String) -> Unit,
    val onClearKeyword: () -> Unit,
    val onSearch: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (String) -> Unit,
)
