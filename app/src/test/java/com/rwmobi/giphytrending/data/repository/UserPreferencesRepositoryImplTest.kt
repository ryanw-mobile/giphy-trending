package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.data.source.preferences.FakePreferencesDataStoreWrapper
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
internal class UserPreferencesRepositoryImplTest : FreeSpec({

    lateinit var repository: UserPreferencesRepositoryImpl
    lateinit var fakePreferencesDataStoreWrapper: FakePreferencesDataStoreWrapper
    lateinit var testScope: TestScope
    lateinit var testDispatcher: TestDispatcher

    beforeTest {
        testDispatcher = UnconfinedTestDispatcher()
        testScope = TestScope(testDispatcher)
        fakePreferencesDataStoreWrapper = FakePreferencesDataStoreWrapper()
        repository = UserPreferencesRepositoryImpl(
            preferencesDataStoreWrapper = fakePreferencesDataStoreWrapper,
            externalCoroutineScope = testScope,
            dispatcher = testDispatcher,
        )
    }

    "setApiMax" - {
        "Given valid max API value, when setApiMax is called, then apiMaxEntries should be updated correctly" {
            testScope.run {
                // Given
                val expectedMaxApiEntries = 50
                // When
                repository.setApiMax(expectedMaxApiEntries)
                // Then
                repository.apiMaxEntries.collect { actualMaxApiEntries ->
                    actualMaxApiEntries shouldBe expectedMaxApiEntries
                }
            }
        }
    }

    "apiMaxEntries" - {
        "Given the repository is initialized, when observing apiMaxEntries, then the default value should be reflected" {
            testScope.run {
                // Given (setup in beforeTest)
                // When & Then
                repository.apiMaxEntries.collect { actualMaxApiEntries ->
                    val expectedValue = (BuildConfig.API_MAX_ENTRIES.toInt() + BuildConfig.API_MIN_ENTRIES.toInt()) / 2
                    actualMaxApiEntries shouldBe expectedValue
                }
            }
        }
    }

    "preferenceErrors" - {
        "Given an error occurs in PreferencesDataStoreWrapper, when preferenceErrors is collected, then the error should be emitted" {
            testScope.run {
                // Given
                val exception = RuntimeException("Test Exception")
                fakePreferencesDataStoreWrapper.emitError(exception)

                // When & Then
                repository.preferenceErrors.collect { actualException ->
                    actualException shouldBe exception
                }
            }
        }
    }
})

//
// @ExperimentalCoroutinesApi
// internal class UserPreferencesRepositoryImplTest : FreeSpec() {
//
//    private lateinit var userPreferencesRepository: UserPreferencesRepository
//    private lateinit var dispatcher: TestDispatcher
//    private lateinit var fakePreferencesDataStoreWrapper: FakePreferencesDataStoreWrapper
//
//    private fun setupRepository() {
//        dispatcher = UnconfinedTestDispatcher()
//        fakePreferencesDataStoreWrapper = FakePreferencesDataStoreWrapper()
//        userPreferencesRepository = UserPreferencesRepositoryImpl(
//            preferencesDataStoreWrapper = fakePreferencesDataStoreWrapper,
//            externalCoroutineScope = TestScope(),
//            dispatcher = dispatcher,
//        )
//    }
//
//    init {
//        "setApiMax" - {
//            "should return success if preference datastore works correctly" {
//                // 🔴 Given
//                setupRepository()
//
//                // 🟡 When
//                val result = userPreferencesRepository.setApiMax(50)
//
//                // 🟢 Then
//                result.isSuccess shouldBe true
//            }
//
//            "should return failure if preference datastore throws exception" {
//                // 🔴 Given
//                setupRepository()
//                fakePreferencesDataStoreWrapper.apiError = Exception()
//
//                // 🟡 When
//                val result = userPreferencesRepository.setApiMax(50)
//
//                // 🟢 Then
//                result.isFailure shouldBe true
//                result.exceptionOrNull()!! shouldBe Exception()
//            }
//        }
//
//        "getApiMax" - {
//            "should return success and correct result if preference datastore works correctly" {
//                // 🔴 Given
//                setupRepository()
//                fakePreferencesDataStoreWrapper.mockGetMaxApiEntriesResponse = flowOf(100)
//
//                // 🟡 When
//                val result = userPreferencesRepository.getApiMax()
//
//                // 🟢 Then
//                result.isSuccess shouldBe true
//                result.getOrNull()!! shouldBe 100
//            }
//
//            "should return failure if preference datastore throws exception" {
//                // 🔴 Given
//                setupRepository()
//                fakePreferencesDataStoreWrapper.apiError = Exception()
//
//                // 🟡 When
//                val result = userPreferencesRepository.getApiMax()
//
//                // 🟢 Then
//                result.isFailure shouldBe true
//                result.exceptionOrNull()!! shouldBe Exception()
//            }
//        }
//    }
// }
