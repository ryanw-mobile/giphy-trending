package com.rwmobi.giphytrending.data.source.preferences

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow

class FakePreferencesDataStoreWrapper : PreferencesDataStoreWrapper {
    private val _preferenceErrors = MutableSharedFlow<Throwable>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val preferenceErrors: SharedFlow<Throwable> = _preferenceErrors

    // Use a backing field to simulate the preferences storage
    private var preferencesStorage: Preferences = preferencesOf()

    // Function to manually emit errors for testing error handling
    suspend fun emitError(exception: Throwable) {
        _preferenceErrors.emit(exception)
    }

    override fun getDataStoreFlow(): Flow<Preferences> {
        return flow {
            emit(preferencesStorage)
        }
    }

    override suspend fun updateIntPreference(key: Preferences.Key<Int>, newValue: Int) {
        preferencesStorage = preferencesStorage.toMutablePreferences().apply {
            this[key] = newValue
        }
    }

    override suspend fun clear() {
        preferencesStorage = preferencesOf()
    }
}
