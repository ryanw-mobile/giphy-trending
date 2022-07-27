package uk.ryanwong.giphytrending.data.source.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesDataStoreWrapper {
    suspend fun setMaxApiEntries(maxEntries: Int)
    suspend fun getMaxApiEntries(): Flow<Int>
}