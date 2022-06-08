package uk.ryanwong.giphytrending.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.Flow

class FakeUserPreferencesRepository : DefaultUserPreferencesRepository {
    private var _apiMaxEntries = MutableLiveData(0)
    override val apiMaxEntries: LiveData<Int>
        get() = _apiMaxEntries

    override suspend fun updateApiMax(apiMax: Int) {
        _apiMaxEntries.value = apiMax
    }

    override fun getApiMax(): Flow<Int> = _apiMaxEntries.asFlow()
}