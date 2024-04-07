/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface UserPreferencesRepository {
    val apiMaxEntries: MutableStateFlow<Int?>
    val preferenceErrors: SharedFlow<Throwable>

    suspend fun setApiMax(apiMax: Int)
}
