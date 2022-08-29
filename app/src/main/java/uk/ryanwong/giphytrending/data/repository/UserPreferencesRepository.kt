package uk.ryanwong.giphytrending.data.repository

interface UserPreferencesRepository {
    suspend fun setApiMax(apiMax: Int): Result<Unit>
    suspend fun getApiMax(): Result<Int>
}
