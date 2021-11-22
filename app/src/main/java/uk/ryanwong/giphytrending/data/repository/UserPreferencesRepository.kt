package uk.ryanwong.giphytrending.data.repository

import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreManager
import javax.inject.Inject

/**
 * Class that handles saving and retrieving user preferences
 * The repository caches the value to avoid repeated asynchronous queries
 * Coroutine is used instead of RxJava here
 */
class UserPreferencesRepository @Inject constructor(private val preferencesDataStoreManager: PreferencesDataStoreManager) :
    DefaultUserPreferencesRepository {

    // Expose preferences live data
    override val apiMaxEntries = preferencesDataStoreManager.apiMaxEntries

    override suspend fun updateApiMax(apiMax: Int) {
        preferencesDataStoreManager.updateMaxApiEntries(apiMax)
    }

    override fun getApiMax() = preferencesDataStoreManager.emitMaxApiEntries()
}