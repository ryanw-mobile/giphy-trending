/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.ui.model.ErrorMessage

data class SettingsUIState(
    val apiRequestLimit: Int? = null,
    val rating: Rating? = null,
    val isLoading: Boolean = true,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
