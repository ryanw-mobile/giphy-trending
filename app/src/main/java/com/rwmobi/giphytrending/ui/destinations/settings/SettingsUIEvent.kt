/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import com.rwmobi.giphytrending.domain.model.Rating

data class SettingsUIEvent(
    val onUpdateApiMaxEntries: (maxApiEntries: Int) -> Unit,
    val onUpdateRating: (rating: Rating) -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
)
