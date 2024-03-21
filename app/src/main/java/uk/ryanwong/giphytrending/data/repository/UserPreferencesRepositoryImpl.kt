package uk.ryanwong.giphytrending.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import uk.ryanwong.giphytrending.BuildConfig
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreWrapper
import uk.ryanwong.giphytrending.di.IoDispatcher
import uk.ryanwong.giphytrending.domain.except
import uk.ryanwong.giphytrending.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Class that handles saving and retrieving user preferences
 * The repository caches the value to avoid repeated asynchronous queries
 * Coroutine is used instead of RxJava here
 */
class UserPreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataStoreWrapper: PreferencesDataStoreWrapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : UserPreferencesRepository {

    override suspend fun setApiMax(apiMax: Int): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                preferencesDataStoreWrapper.setMaxApiEntries(apiMax)
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getApiMax(): Result<Int> {
        return withContext(dispatcher) {
            Result.runCatching {
                val flow = preferencesDataStoreWrapper.getMaxApiEntries()
                val apiMax = flow.firstOrNull() ?: BuildConfig.API_MAX_ENTRIES.toInt()
                apiMax
            }.except<CancellationException, _>()
        }
    }
}
