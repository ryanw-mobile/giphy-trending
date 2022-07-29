package uk.ryanwong.giphytrending.data.source.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockPreferencesDataStoreWrapper : PreferencesDataStoreWrapper {
    var apiError: Throwable? = null

    override suspend fun setMaxApiEntries(maxEntries: Int) {
        apiError?.run { throw this }
    }

    var mockGetMaxApiEntriesResponse: Flow<Int>? = null
    override suspend fun getMaxApiEntries(): Flow<Int> {
        apiError?.run { throw this }
        return mockGetMaxApiEntriesResponse ?: flowOf(1)
    }
}