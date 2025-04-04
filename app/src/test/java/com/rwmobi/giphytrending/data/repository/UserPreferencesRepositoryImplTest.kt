/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rwmobi.giphytrending.data.source.preferences.FakePreferencesDataStoreWrapper
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.model.UserPreferences
import com.rwmobi.giphytrending.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class UserPreferencesRepositoryImplTest {
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

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        testScope = TestScope(dispatcher)
        fakePreferencesDataStoreWrapper = FakePreferencesDataStoreWrapper()
    }

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `propagates error when DataStore throws exception`() = runTest {
        // Testing sharedFlow: https://developer.android.com/kotlin/flow/test#continuous-collection
        runTest(dispatcher) {
            val expectedException = Exception("some error message")
            val values = mutableListOf<Throwable>()
            setupRepository()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                userPreferencesRepository.preferenceErrors.toList(values)
            }

            fakePreferencesDataStoreWrapper.emitError(expectedException)

            assertEquals(expectedException, values[0])
        }
    }

    @Test
    fun `uses default preferences when DataStore is empty`() = runTest {
        setupRepository()
        assertEquals(
            UserPreferences(
                apiRequestLimit = ((defaultApiMaxEntries + defaultApiMinEntries) / 2),
                rating = Rating.G,
            ),
            userPreferencesRepository.userPreferences.value,
        )
    }

    @Test
    fun `reflects stored preferences when DataStore has values`() = runTest {
        fakePreferencesDataStoreWrapper.updatePreference(key = intPreferencesKey(KEY_API_REQUEST_LIMIT), newValue = 100)
        fakePreferencesDataStoreWrapper.updatePreference(key = stringPreferencesKey(KEY_RATING), newValue = Rating.R.apiValue)

        setupRepository()

        assertEquals(
            UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.R,
            ),
            userPreferencesRepository.userPreferences.value,
        )
    }

    @Test
    fun `updates api request limit correctly`() = runTest {
        fakePreferencesDataStoreWrapper.updatePreference(key = intPreferencesKey(KEY_API_REQUEST_LIMIT), newValue = 100)
        fakePreferencesDataStoreWrapper.updatePreference(key = stringPreferencesKey(KEY_RATING), newValue = Rating.R.apiValue)
        setupRepository()

        userPreferencesRepository.setApiRequestLimit(limit = 50)

        assertEquals(
            UserPreferences(
                apiRequestLimit = 50,
                rating = Rating.R,
            ),
            userPreferencesRepository.userPreferences.value,
        )
    }

    @Test
    fun `updates rating correctly`() = runTest {
        fakePreferencesDataStoreWrapper.updatePreference(key = intPreferencesKey(KEY_API_REQUEST_LIMIT), newValue = 100)
        fakePreferencesDataStoreWrapper.updatePreference(key = stringPreferencesKey(KEY_RATING), newValue = Rating.R.apiValue)
        setupRepository()

        userPreferencesRepository.setRating(rating = Rating.G)

        assertEquals(
            UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
            userPreferencesRepository.userPreferences.value,
        )
    }
}
