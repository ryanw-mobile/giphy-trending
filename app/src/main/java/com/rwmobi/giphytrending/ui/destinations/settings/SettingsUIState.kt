/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import com.rwmobi.giphytrending.ui.model.ErrorMessage

data class SettingsUIState(
    val apiMaxEntries: Int? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
