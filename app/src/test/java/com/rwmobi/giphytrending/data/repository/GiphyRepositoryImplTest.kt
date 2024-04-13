/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.source.local.FakeRoomDbDataSource
import com.rwmobi.giphytrending.data.source.local.toDomainModelList
import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSource
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntityList
import com.rwmobi.giphytrending.test.testdata.SampleTrendingNetworkResponse
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
internal class GiphyRepositoryImplTest : FreeSpec() {
    private lateinit var giphyRepository: GiphyRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeRoomDbDataSource: FakeRoomDbDataSource
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    private fun setupRepository(giphyApiKey: String = "some-api-key") {
        dispatcher = UnconfinedTestDispatcher()
        fakeRoomDbDataSource = FakeRoomDbDataSource()
        fakeNetworkDataSource = FakeNetworkDataSource()
        giphyRepository = GiphyRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            roomDbDataSource = fakeRoomDbDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }

    init {
        "fetchCachedTrending" - {
            "should return correct list if database query success" {
                setupRepository()
                fakeRoomDbDataSource.mockQueryDataResponse = SampleTrendingEntityList.jobBidenEntity

                val result = giphyRepository.fetchCachedTrending()

                result.isSuccess shouldBe true
                result.getOrNull()!! shouldBe SampleTrendingEntityList.jobBidenEntity.toDomainModelList()
            }

            "should return failure if database query throws exception" {
                setupRepository()
                fakeRoomDbDataSource.apiError = Exception()

                val result = giphyRepository.fetchCachedTrending()

                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }
        }

        "reloadTrending" - {
            "should return correct list if network and database operations all success" {
                setupRepository()
                fakeNetworkDataSource.mockTrendingNetworkResponse = SampleTrendingNetworkResponse.jobBidenResponse
                fakeRoomDbDataSource.mockQueryDataResponse = SampleTrendingEntityList.jobBidenEntity

                val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

                result.isSuccess shouldBe true
                result.getOrNull()!! shouldBe SampleTrendingEntityList.jobBidenEntity.toDomainModelList()
            }

            "should return failure if giphyApiKey is blank" {
                setupRepository(giphyApiKey = "")
                fakeNetworkDataSource.apiError = Exception()

                val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe EmptyGiphyAPIKeyException()
            }

            "should return failure if network call returns an error" {
                setupRepository()
                fakeNetworkDataSource.apiError = Exception()

                val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }

            "should return failure if database operation throws exception" {
                setupRepository()
                fakeRoomDbDataSource.apiError = Exception()

                val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

                result.isFailure shouldBe true
                result.exceptionOrNull()!! shouldBe Exception()
            }
        }
    }
}
