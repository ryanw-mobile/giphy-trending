package uk.ryanwong.giphytrending.data.repository

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun updateApiMax(apiMax: Int): Job
    fun getApiMax(): Flow<Int>
}