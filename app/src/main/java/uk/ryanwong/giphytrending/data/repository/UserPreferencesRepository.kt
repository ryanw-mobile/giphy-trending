package uk.ryanwong.giphytrending.data.repository

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreManager

/**
 * Class that handles saving and retrieving user preferences
 * The repository caches the value to avoid repeated asynchronous queries
 * Coroutine is used instead of RxJava here
 */
class UserPreferencesRepository(private val preferencesDataStoreManager: PreferencesDataStoreManager) {

    // Expose preferences live data
    val apiMaxEntries = preferencesDataStoreManager.apiMaxEntries

    fun updateApiMax(apiMax: Int) = runBlocking {
        launch { preferencesDataStoreManager.updateMaxApiEntries(apiMax) }
    }

    fun getApiMax() = preferencesDataStoreManager.emitMaxApiEntries()
}