package com.rwmobi.giphytrending.data.repository

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rwmobi.giphytrending.data.source.preferences.FakePreferencesDataStoreWrapper
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
internal class UserPreferencesRepositoryImplTest : FreeSpec() {
    private val defaultApiMinEntries: Int = 25
    private val defaultApiMaxEntries: Int = 100

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var testScope: TestScope
    private lateinit var fakePreferencesDataStoreWrapper: FakePreferencesDataStoreWrapper

    private fun setupRepository() {
        userPreferencesRepository = UserPreferencesRepositoryImpl(
            preferencesDataStoreWrapper = fakePreferencesDataStoreWrapper,
            defaultApiMinEntries = defaultApiMinEntries,
            defaultApiMaxEntries = defaultApiMaxEntries,
            externalCoroutineScope = testScope,
            dispatcher = dispatcher,
        )
    }

    init {
        beforeTest {
            dispatcher = UnconfinedTestDispatcher()
            testScope = TestScope(dispatcher)
            fakePreferencesDataStoreWrapper = FakePreferencesDataStoreWrapper()
        }

        "init" - {
            "when datastore returns exception, it propagates the error" {
                // Testing sharedFlow: https://developer.android.com/kotlin/flow/test#continuous-collection
                runTest(dispatcher) {
                    // Given
                    val expectedException = Exception("some error message")
                    val values = mutableListOf<Throwable>()
                    setupRepository()
                    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                        userPreferencesRepository.preferenceErrors.toList(values)
                    }

                    // When
                    fakePreferencesDataStoreWrapper.emitError(expectedException)

                    // Then
                    values[0] shouldBe expectedException
                }
            }

            "when datastore returns empty results, it propagates the default user preferences" {
                // When
                setupRepository()

                // Then
                userPreferencesRepository.userPreferences.value shouldBe UserPreferences(
                    apiRequestLimit = ((defaultApiMaxEntries + defaultApiMinEntries) / 2),
                    rating = Rating.G,
                )
            }

            "when datastore returns success result, it propagates the user preferences" {
                // Given
                fakePreferencesDataStoreWrapper.updatePreference(key = intPreferencesKey(KEY_API_REQUEST_LIMIT), newValue = 100)
                fakePreferencesDataStoreWrapper.updatePreference(key = stringPreferencesKey(KEY_RATING), newValue = Rating.R.apiValue)

                // When
                setupRepository()

                // Then
                userPreferencesRepository.userPreferences.value shouldBe UserPreferences(
                    apiRequestLimit = 100,
                    rating = Rating.R,
                )
            }
        }

        "setApiRequestLimit" - {
            "when success, it updates the userPreferences stateFlow" {
                // Given
                fakePreferencesDataStoreWrapper.updatePreference(key = intPreferencesKey(KEY_API_REQUEST_LIMIT), newValue = 100)
                fakePreferencesDataStoreWrapper.updatePreference(key = stringPreferencesKey(KEY_RATING), newValue = Rating.R.apiValue)
                setupRepository()

                // When
                userPreferencesRepository.setApiRequestLimit(limit = 50)

                // Then
                userPreferencesRepository.userPreferences.value shouldBe UserPreferences(
                    apiRequestLimit = 50,
                    rating = Rating.R,
                )
            }
        }

        "setRating" - {
            "when success, it updates the userPreferences stateFlow" {
                // Given
                fakePreferencesDataStoreWrapper.updatePreference(key = intPreferencesKey(KEY_API_REQUEST_LIMIT), newValue = 100)
                fakePreferencesDataStoreWrapper.updatePreference(key = stringPreferencesKey(KEY_RATING), newValue = Rating.R.apiValue)
                setupRepository()

                // When
                userPreferencesRepository.setRating(rating = Rating.G)

                // Then
                userPreferencesRepository.userPreferences.value shouldBe UserPreferences(
                    apiRequestLimit = 100,
                    rating = Rating.G,
                )
            }
        }
    }
}
