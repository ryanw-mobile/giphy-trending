/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.repository.mappers.toGifObject
import com.rwmobi.giphytrending.data.source.local.FakeDatabaseDataSource
import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSource
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntityList
import com.rwmobi.giphytrending.test.testdata.SampleTrendingNetworkResponse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TrendingRepositoryImplTest {
    private lateinit var trendingRepository: TrendingRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeRoomDbDataSource: FakeDatabaseDataSource
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    private fun setupRepository(giphyApiKey: String = "some-api-key") {
        dispatcher = UnconfinedTestDispatcher()
        fakeRoomDbDataSource = FakeDatabaseDataSource()
        fakeNetworkDataSource = FakeNetworkDataSource()
        trendingRepository = TrendingRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            databaseDataSource = fakeRoomDbDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun fetchCachedTrending_ShouldReturnCorrectList_WhenDatabaseQuerySucceeds() = runTest {
        setupRepository()
        fakeRoomDbDataSource.queryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = trendingRepository.fetchCachedTrending()

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }
    }

    @Test
    fun fetchCachedTrending_ShouldReturnFailure_WhenDatabaseQueryFails() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = trendingRepository.fetchCachedTrending()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun reloadTrending_ShouldReturnCorrectList_WhenNetworkAndDatabaseOperationsSucceed() = runTest {
        setupRepository()
        fakeNetworkDataSource.trendingNetworkResponseDto = SampleTrendingNetworkResponse.singleResponse
        fakeRoomDbDataSource.queryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenApiKeyIsBlank() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe EmptyGiphyAPIKeyException()
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenNetworkCallFails() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenDatabaseOperationThrowsException() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }
}
