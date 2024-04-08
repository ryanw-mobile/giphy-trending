package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

class FakeUserPreferencesRepository : UserPreferencesRepository {
    override val apiMaxEntries: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors: SharedFlow<Throwable> = _preferenceErrors

    suspend fun emitError(exception: Throwable) {
        _preferenceErrors.emit(exception)
    }

    override suspend fun setApiMax(apiMax: Int) {
        apiMaxEntries.value = apiMax
    }
}
