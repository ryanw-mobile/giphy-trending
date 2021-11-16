package uk.ryanwong.giphytrending.data.source.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import uk.ryanwong.giphytrending.BuildConfig
import java.io.IOException
import javax.inject.Inject


private const val USER_PREFERENCES_NAME = "user_preferences"
private const val KEY_API_MAX_ENTRIES = "api_max_entries"

/***
 * Default Coroutine approach of Preferences DataStore implementation.
 * Not considering RxJava because Coroutine is more future proof in the Android-Kotlin world
 */
class PreferencesDataStoreManager @Inject constructor(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

    val apiMaxEntries: LiveData<Int> = context.dataStore.data.asLiveData()
        .map { preferences ->
            // No type safety.
            preferences[API_MAX_ENTRIES] ?: BuildConfig.API_MAX_ENTRIES.toInt()
        }

    companion object {
        val API_MAX_ENTRIES = intPreferencesKey(KEY_API_MAX_ENTRIES)
    }

    suspend fun updateMaxApiEntries(maxEntries: Int = BuildConfig.API_MAX_ENTRIES.toInt()) {
        context.dataStore.edit { settings ->
            settings[API_MAX_ENTRIES] = maxEntries
        }
    }

    fun emitMaxApiEntries(): Flow<Int> {
        return context.dataStore.data.catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { settings ->
            settings[API_MAX_ENTRIES] ?: BuildConfig.API_MAX_ENTRIES.toInt()
        }
    }
}