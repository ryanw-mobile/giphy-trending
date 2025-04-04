/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.ui.model.ErrorMessage

data class TrendingUIState(
    val gifObjects: List<GifObject>? = null,
    val isLoading: Boolean = true,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
