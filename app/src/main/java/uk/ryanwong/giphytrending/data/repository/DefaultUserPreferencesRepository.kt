package uk.ryanwong.giphytrending.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface DefaultUserPreferencesRepository {
    // Expose preferences live data
    val apiMaxEntries: LiveData<Int>
    fun updateApiMax(apiMax: Int)
    fun getApiMax(): Flow<Int>
}