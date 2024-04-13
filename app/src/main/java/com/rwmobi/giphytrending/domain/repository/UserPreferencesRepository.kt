/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesRepository {
    val userPreferences: StateFlow<UserPreferences>
    val preferenceErrors: SharedFlow<Throwable>

    suspend fun setApiRequestLimit(limit: Int)
    suspend fun setRating(rating: Rating)
}
