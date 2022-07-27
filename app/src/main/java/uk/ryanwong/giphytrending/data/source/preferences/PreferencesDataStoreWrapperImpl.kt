package uk.ryanwong.giphytrending.data.source.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import uk.ryanwong.giphytrending.BuildConfig
import java.io.IOException
import javax.inject.Inject

private const val KEY_API_MAX_ENTRIES = "api_max_entries"

class PreferencesDataStoreWrapperImpl @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
) : PreferencesDataStoreWrapper {
    private val API_MAX_ENTRIES = intPreferencesKey(KEY_API_MAX_ENTRIES)

    override suspend fun setMaxApiEntries(maxEntries: Int) {
        dataStorePreferences.edit { settings ->
            settings[API_MAX_ENTRIES] = maxEntries
        }
    }

    override suspend fun getMaxApiEntries(): Flow<Int> {
        return dataStorePreferences.data.catch { exception ->
            // dataStore.data throws IOException when error while reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[API_MAX_ENTRIES] ?: BuildConfig.API_MAX_ENTRIES.toInt()
        }
    }
}