package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import uk.ryanwong.giphytrending.domain.repository.UserPreferencesRepository

class MockUserPreferencesRepository : UserPreferencesRepository {

    private var mockApiMaxResponse = -1
    var mockSetApiMaxResponse: Result<Unit>? = null
    override val apiMaxEntries: MutableStateFlow<Int?>
        get() = TODO("Not yet implemented")
    override val preferenceErrors: SharedFlow<Throwable>
        get() = TODO("Not yet implemented")

    override suspend fun setApiMax(apiMax: Int): Result<Unit> {
        mockApiMaxResponse = apiMax
        return mockSetApiMaxResponse ?: Result.success(Unit)
    }

    var mockGetApiMaxResponse: Result<Int>? = null
    override suspend fun getApiMax(): Result<Int> {
        return mockGetApiMaxResponse ?: Result.success(mockApiMaxResponse)
    }
}
