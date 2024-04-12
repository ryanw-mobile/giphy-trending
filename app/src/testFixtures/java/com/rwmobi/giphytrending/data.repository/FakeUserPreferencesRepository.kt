package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesRepository : UserPreferencesRepository {
    private val _userPreferences: MutableStateFlow<UserPreferences> = MutableStateFlow(UserPreferences(apiRequestLimit = null, rating = null))
    override val userPreferences = _userPreferences.asStateFlow()

    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors: SharedFlow<Throwable> = _preferenceErrors

    suspend fun emitError(exception: Throwable) {
        _preferenceErrors.emit(exception)
    }

    override suspend fun setApiRequestLimit(limit: Int) {
        _userPreferences.update {
            it.copy(
                apiRequestLimit = limit,
            )
        }
    }

    override suspend fun setRating(rating: Rating) {
        _userPreferences.update {
            it.copy(
                rating = rating,
            )
        }
    }

    fun init(userPreferences: UserPreferences) {
        _userPreferences.update {
            it.copy(
                rating = userPreferences.rating,
                apiRequestLimit = userPreferences.apiRequestLimit,
            )
        }
    }
}
