package uk.ryanwong.giphytrending.data.repository

interface UserPreferencesRepository {
    suspend fun setApiMax(apiMax: Int)
    suspend fun getApiMax(): Result<Int>
}