/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

data class SettingsUIEvent(
    val onUpdateApiMaxEntries: (maxApiEntries: Int) -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
)
