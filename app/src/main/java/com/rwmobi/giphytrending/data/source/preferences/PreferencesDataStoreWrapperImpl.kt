/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rwmobi.giphytrending.data.source.preferences.interfaces.PreferencesDataStoreWrapper
import com.rwmobi.giphytrending.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesDataStoreWrapperImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : PreferencesDataStoreWrapper {
    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors = _preferenceErrors.asSharedFlow()

    override fun getDataStoreFlow(): Flow<Preferences> = dataStore.data

    override suspend fun <T> updatePreference(key: Preferences.Key<T>, newValue: T) {
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
