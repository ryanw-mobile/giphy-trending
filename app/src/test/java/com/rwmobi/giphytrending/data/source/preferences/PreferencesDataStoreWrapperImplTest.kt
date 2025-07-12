/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.preferences

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.platform.app.InstrumentationRegistry
import com.rwmobi.giphytrending.data.source.preferences.interfaces.PreferencesDataStoreWrapper
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

private const val TEST_DATASTORE_NAME: String = "test_datastore"

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class PreferencesDataStoreWrapperImplTest {
    // Reference: https://medium.com/androiddevelopers/datastore-and-testing-edf7ae8df3d8
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TEST_DATASTORE_NAME)
    private lateinit var preferencesDataStoreWrapper: PreferencesDataStoreWrapper

    private val prefKeyString = stringPreferencesKey("keyString")
    private val prefKeyBoolean = booleanPreferencesKey("keyBoolean")
    private val prefKeyInt = intPreferencesKey("keyInt")

    @Before
    fun setup() {
        val testContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        preferencesDataStoreWrapper = PreferencesDataStoreWrapperImpl(
            dataStore = testContext.dataStore,
            dispatcher = Dispatchers.Unconfined,
        )
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun updatePreference_ShouldCorrectlyUpdateStringValue() = runTest {
        val newValue = "newString"
        // Start collecting the flow in a separate coroutine to observe updates.
        val dataFlow = preferencesDataStoreWrapper.getDataStoreFlow()

        // Update the preference and wait for the flow to collect the new value.
        preferencesDataStoreWrapper.updatePreference(key = prefKeyString, newValue = newValue)

        val flowJob = launch {
            dataFlow.collect { preferences ->
                if (preferences[prefKeyString] == newValue) {
                    assertEquals(newValue, preferences[prefKeyString])
                    cancel() // Cancel the job once we've observed the update
                }
            }
        }
        flowJob.join() // Wait for the job to complete
    }

    @Test
    fun updatePreference_ShouldCorrectlyUpdateBooleanValue() = runTest {
        val newValue = true
        val dataFlow = preferencesDataStoreWrapper.getDataStoreFlow()

        preferencesDataStoreWrapper.updatePreference(key = prefKeyBoolean, newValue = newValue)

        val flowJob = launch {
            dataFlow.collect { preferences ->
                if (preferences[prefKeyBoolean] == newValue) {
                    assertEquals(newValue, preferences[prefKeyBoolean])
                    cancel()
                }
            }
        }
        flowJob.join()
    }

    @Test
    fun updatePreference_ShouldCorrectlyUpdateIntValue() = runTest {
        val newValue = 42
        val dataFlow = preferencesDataStoreWrapper.getDataStoreFlow()

        preferencesDataStoreWrapper.updatePreference(key = prefKeyInt, newValue = newValue)

        val flowJob = launch {
            dataFlow.collect { preferences ->
                if (preferences[prefKeyInt] == newValue) {
                    assertEquals(newValue, preferences[prefKeyInt])
                    cancel() // Cancel the job once we've observed the update
                }
            }
        }
        flowJob.join()
    }

    @Test
    fun clearPreferences_ShouldRemoveAllValues() = runTest {
        // Setup initial values
        with(preferencesDataStoreWrapper) {
            updatePreference(key = prefKeyString, newValue = "newString")
            updatePreference(key = prefKeyBoolean, newValue = true)
            updatePreference(key = prefKeyInt, newValue = 42)
        }
        val dataFlow = preferencesDataStoreWrapper.getDataStoreFlow()

        preferencesDataStoreWrapper.clear()

        val flowJob = launch {
            dataFlow.collect { preferences ->
                if (preferences[prefKeyString] == null && preferences[prefKeyBoolean] == null && preferences[prefKeyInt] == null) {
                    assertEquals(null, preferences[prefKeyString])
                    assertEquals(null, preferences[prefKeyBoolean])
                    assertEquals(null, preferences[prefKeyInt])
                    cancel()
                }
            }
        }
        flowJob.join()
    }
}
