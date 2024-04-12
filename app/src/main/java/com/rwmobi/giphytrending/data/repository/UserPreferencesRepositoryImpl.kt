/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import androidx.datastore.preferences.core.intPreferencesKey
import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.data.source.preferences.PreferencesDataStoreWrapper
import com.rwmobi.giphytrending.di.DispatcherModule
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

private const val KEY_API_MAX_ENTRIES = "api_max_entries"

class UserPreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataStoreWrapper: PreferencesDataStoreWrapper,
    private val defaultApiMinEntries: Int = BuildConfig.API_MIN_ENTRIES.toInt(),
    private val defaultApiMaxEntries: Int = BuildConfig.API_MAX_ENTRIES.toInt(),
    externalCoroutineScope: CoroutineScope,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : UserPreferencesRepository {
    private val prefKeyMaxApiEntries = intPreferencesKey(KEY_API_MAX_ENTRIES)

    private val _apiMaxEntries: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val apiMaxEntries = _apiMaxEntries

    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors = _preferenceErrors.asSharedFlow()

    init {
        externalCoroutineScope.launch(dispatcher) {
            launch {
                preferencesDataStoreWrapper.preferenceErrors.collect { _preferenceErrors.emit(it) }
            }

            launch {
                preferencesDataStoreWrapper.getDataStoreFlow()
                    .catch { exception ->
                        _preferenceErrors.emit(exception)
                    }
                    .collect { prefs ->
                        _apiMaxEntries.value = prefs[prefKeyMaxApiEntries] ?: ((defaultApiMaxEntries + defaultApiMinEntries) / 2)
                    }
            }
        }
    }

    override suspend fun setApiMax(apiMax: Int) {
        preferencesDataStoreWrapper.updatePreference(
            key = prefKeyMaxApiEntries,
            newValue = max(min(apiMax, defaultApiMaxEntries), defaultApiMinEntries),
        )
    }
}
