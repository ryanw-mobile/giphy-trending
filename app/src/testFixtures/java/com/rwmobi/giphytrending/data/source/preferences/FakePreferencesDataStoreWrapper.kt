package com.rwmobi.giphytrending.data.source.preferences

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakePreferencesDataStoreWrapper : PreferencesDataStoreWrapper {
    private val _preferences = MutableStateFlow<Preferences>(emptyPreferences())

    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors: SharedFlow<Throwable> = _preferenceErrors.asSharedFlow()

    override fun getDataStoreFlow(): Flow<Preferences> = _preferences

    override suspend fun <T> updatePreference(key: Preferences.Key<T>, newValue: T) {
        _preferences.value = _preferences.value.toMutablePreferences().apply {
            this[key] = newValue
        }
    }

    override suspend fun clear() {
        _preferences.value = emptyPreferences()
    }

    suspend fun emitError(exception: Throwable) {
        _preferenceErrors.emit(exception)
    }
}
