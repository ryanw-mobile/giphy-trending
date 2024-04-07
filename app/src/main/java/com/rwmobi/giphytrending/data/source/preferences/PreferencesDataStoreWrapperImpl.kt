/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

class PreferencesDataStoreWrapperImpl(
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
) : PreferencesDataStoreWrapper {
    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors = _preferenceErrors.asSharedFlow()

    override fun getDataStoreFlow(): Flow<Preferences> = dataStore.data

    override suspend fun updateIntPreference(key: Preferences.Key<Int>, newValue: Int) {
        withContext(dispatcher) {
            try {
                dataStore.edit { mutablePreferences ->
                    mutablePreferences[key] = newValue
                }
            } catch (e: Throwable) {
                _preferenceErrors.emit(e)
            }
        }
    }

    override suspend fun clear() {
        withContext(dispatcher) {
            try {
                dataStore.edit { mutablePreferences ->
                    mutablePreferences.clear()
                }
            } catch (e: Throwable) {
                _preferenceErrors.emit(e)
            }
        }
    }
}
