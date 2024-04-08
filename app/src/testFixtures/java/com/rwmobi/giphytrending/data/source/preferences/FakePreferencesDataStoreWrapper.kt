package com.rwmobi.giphytrending.data.source.preferences

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf

class FakePreferencesDataStoreWrapper : PreferencesDataStoreWrapper {
    override val preferenceErrors: SharedFlow<Throwable>
        get() = TODO("Not yet implemented")

    override fun getDataStoreFlow(): Flow<Preferences> {
        TODO("Not yet implemented")
    }

    override suspend fun updateIntPreference(key: Preferences.Key<Int>, newValue: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    var apiError: Throwable? = null

//    override suspend fun setMaxApiEntries(maxEntries: Int) {
//        apiError?.run { throw this }
//    }
//
//    var mockGetMaxApiEntriesResponse: Flow<Int>? = null
//    override suspend fun getMaxApiEntries(): Flow<Int> {
//        apiError?.run { throw this }
//        return mockGetMaxApiEntriesResponse ?: flowOf(1)
//    }

}
