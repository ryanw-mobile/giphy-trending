package uk.ryanwong.giphytrending.data.repository

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.giphytrending.data.source.preferences.MockPreferencesDataStoreWrapper

@ExperimentalCoroutinesApi
internal class UserPreferencesRepositoryImplTest : FreeSpec() {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var mockPreferencesDataStoreWrapper: MockPreferencesDataStoreWrapper

    private fun setupRepository() {
        dispatcher = UnconfinedTestDispatcher()
        mockPreferencesDataStoreWrapper = MockPreferencesDataStoreWrapper()
        userPreferencesRepository = UserPreferencesRepositoryImpl(
            preferencesDataStoreWrapper = mockPreferencesDataStoreWrapper,
            dispatcher = dispatcher
        )
    }

    init {
        "setApiMax" - {
            "should return success if preference datastore works correctly" {
                // 🔴 Given
                setupRepository()

                // 🟡 When
                val result = userPreferencesRepository.setApiMax(50)

                // 🟢 Then
                result.isSuccess shouldBe true
            }

            "should return failure if preference datastore throws exception" {
                // 🔴 Given
                setupRepository()
                mockPreferencesDataStoreWrapper.apiError = Exception()

                // 🟡 When
                val result = userPreferencesRepository.setApiMax(50)

                // 🟢 Then
                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }
        }

        "getApiMax" - {
            "should return success and correct result if preference datastore works correctly" {
                // 🔴 Given
                setupRepository()
                mockPreferencesDataStoreWrapper.mockGetMaxApiEntriesResponse = flowOf(100)

                // 🟡 When
                val result = userPreferencesRepository.getApiMax()

                // 🟢 Then
                result.isSuccess shouldBe true
                result.getOrNull()!! shouldBe 100
            }

            "should return failure if preference datastore throws exception" {
                // 🔴 Given
                setupRepository()
                mockPreferencesDataStoreWrapper.apiError = Exception()

                // 🟡 When
                val result = userPreferencesRepository.getApiMax()

                // 🟢 Then
                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }
        }
    }
}
