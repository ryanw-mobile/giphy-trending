/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.preferences

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesDataStoreWrapper {
    val preferenceErrors: SharedFlow<Throwable>
    fun getDataStoreFlow(): Flow<Preferences>

    suspend fun <T> updatePreference(key: Preferences.Key<T>, newValue: T)

    suspend fun clear()
}
