/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.model.ErrorMessage

data class SearchUIState(
    val keyword: String = "",
    // ⚠️ null and empty can be handled differently by the UI
    val keywordMaxLength: Int = 0,
    val giphyImageItems: List<GiphyImageItem>? = null,
    val isLoading: Boolean = true,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
