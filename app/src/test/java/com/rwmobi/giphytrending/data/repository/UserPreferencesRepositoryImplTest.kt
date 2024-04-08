package com.rwmobi.giphytrending.data.repository

import io.kotest.core.spec.style.FreeSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class UserPreferencesRepositoryImplTest : FreeSpec(
    {

//        lateinit var repository: UserPreferencesRepositoryImpl
//        lateinit var fakePreferencesDataStoreWrapper: FakePreferencesDataStoreWrapper
//        lateinit var testScope: TestScope
//        lateinit var testDispatcher: TestDispatcher
//
//        beforeTest {
//            testDispatcher = UnconfinedTestDispatcher()
//            testScope = TestScope(testDispatcher)
//            fakePreferencesDataStoreWrapper = FakePreferencesDataStoreWrapper()
//            repository = UserPreferencesRepositoryImpl(
//                preferencesDataStoreWrapper = fakePreferencesDataStoreWrapper,
//                externalCoroutineScope = testScope,
//                dispatcher = testDispatcher,
//            )
//        }
//
//        "setApiMax" - {
//            "Given valid max API value, when setApiMax is called, then apiMaxEntries should be updated correctly" - {
//                runTest {
//                    // Given
//                    val expectedMaxApiEntries = 50
//
//                    // When
//                    repository.setApiMax(expectedMaxApiEntries)
//
//                    // Then
//                    repository.apiMaxEntries.value shouldBe expectedMaxApiEntries
//                }
//            }
//        }
//
//        "apiMaxEntries" - {
//            "Given the repository is initialized, when observing apiMaxEntries, then the default value should be reflected" - {
//                runTest {
//                    // Given (setup in beforeTest)
//                    val expectedValue = (BuildConfig.API_MAX_ENTRIES.toInt() + BuildConfig.API_MIN_ENTRIES.toInt()) / 2
//
//                    // When & Then
//                    repository.apiMaxEntries.value shouldBe expectedValue
//                }
//            }
//        }
//
//        "preferenceErrors" - {
//            "Given an error occurs in PreferencesDataStoreWrapper, when preferenceErrors is collected, then the error should be emitted" - {
//                runTest {
//                    // Given
//                    val exception = RuntimeException("Test Exception")
//                    fakePreferencesDataStoreWrapper.emitError(exception)
//
//                    // Prepare to collect emitted errors
//                    val errors = mutableListOf<Throwable>()
//
//                    // Act - Collect the first emitted error
//                    repository.preferenceErrors.take(1).toList(errors)
//
//                    // Then
//                    errors.size shouldBe 1
//                    errors.first() shouldBe exception
//                }
//            }
//        }
    },
)
