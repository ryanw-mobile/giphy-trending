package uk.ryanwong.giphytrending.data.repository

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreManager
import javax.inject.Inject

/**
 * Class that handles saving and retrieving user preferences
 * The repository caches the value to avoid repeated asynchronous queries
 * Coroutine is used instead of RxJava here
 */
class UserPreferencesRepositoryImpl @Inject constructor(private val preferencesDataStoreManager: PreferencesDataStoreManager) :
    UserPreferencesRepository {

    // Expose preferences live data
    val apiMaxEntries = preferencesDataStoreManager.apiMaxEntries

    override fun updateApiMax(apiMax: Int) = runBlocking {
        launch { preferencesDataStoreManager.updateMaxApiEntries(apiMax) }
    }

    override fun getApiMax() = preferencesDataStoreManager.emitMaxApiEntries()
}