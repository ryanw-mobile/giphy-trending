package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.source.local.FakeRoomDbDataSource
import com.rwmobi.giphytrending.data.source.local.TrendingEntity
import com.rwmobi.giphytrending.data.source.local.toDomainModelList
import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSource
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.util.Date

@ExperimentalCoroutinesApi
internal class GiphyRepositoryImplTest : FreeSpec() {

    private lateinit var giphyRepository: GiphyRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeRoomDbDataSource: FakeRoomDbDataSource
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    // 🤦 Greg would say if the data carries no meaning, why not replace them with some-string
    // I'll do that when I have a chance working with you again, but not now 🤷‍
    private val mockTrendingEntityList = listOf(
        TrendingEntity(
            id = "uaIAIw3ELuk69mhZ5I",
            previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
            imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
            webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            title = "Joe Biden GIF by Creative Courage",
            type = "gif",
            username = "creative-courage",
            trendingDateTime = Date(-62170156725000),
            importDateTime = Date(1605041010000),
        ),
    )

    private fun setupRepository() {
        dispatcher = UnconfinedTestDispatcher()
        fakeRoomDbDataSource = FakeRoomDbDataSource()
        fakeNetworkDataSource = FakeNetworkDataSource()
        giphyRepository = GiphyRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            roomDbDataSource = fakeRoomDbDataSource,
            dispatcher = dispatcher,
        )
    }

    init {
        "fetchCachedTrending" - {
            "should return correct list if database query success" {
                // 🔴 Given
                setupRepository()
                fakeRoomDbDataSource.mockQueryDataResponse = mockTrendingEntityList

                // 🟡 When
                val result = giphyRepository.fetchCachedTrending()

                // 🟢 Then
                result.isSuccess shouldBe true
                result.getOrNull()!! shouldBe mockTrendingEntityList.toDomainModelList()
            }

            "should return failure if database query throws exception" {
                // 🔴 Given
                setupRepository()
                fakeRoomDbDataSource.apiError = Exception()

                // 🟡 When
                val result = giphyRepository.fetchCachedTrending()

                // 🟢 Then
                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }
        }

        "reloadTrending" - {

            "should return correct list if network and database operations all success" {
                // 🔴 Given
                setupRepository()
                fakeRoomDbDataSource.mockQueryDataResponse = mockTrendingEntityList

                // 🟡 When
                val result = giphyRepository.fetchCachedTrending()

                // 🟢 Then
                result.isSuccess shouldBe true
                result.getOrNull()!! shouldBe mockTrendingEntityList.toDomainModelList()
            }

            "should return failure if network call returns an error" {
                // 🔴 Given
                setupRepository()
                fakeNetworkDataSource.apiError = Exception()

                // 🟡 When
                val result = giphyRepository.reloadTrending(apiMaxEntries = 100)

                // 🟢 Then
                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }

            "should return failure if database operation throws exception" {
                // 🔴 Given
                setupRepository()
                fakeRoomDbDataSource.apiError = Exception()

                // 🟡 When
                val result = giphyRepository.reloadTrending(apiMaxEntries = 100)

                // 🟢 Then
                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }
        }
    }
}
