/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

data class TrendingUIEvent(
    val onRefresh: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
)
