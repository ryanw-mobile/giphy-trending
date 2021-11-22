package uk.ryanwong.giphytrending.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface DefaultUserPreferencesRepository {
    // Expose preferences live data
    val apiMaxEntries: LiveData<Int>
    suspend fun updateApiMax(apiMax: Int)
    fun getApiMax(): Flow<Int>
}