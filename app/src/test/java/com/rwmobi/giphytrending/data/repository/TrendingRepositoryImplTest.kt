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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

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

        assertTrue(result.isSuccess)
        assertEquals(SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }, result.getOrNull())
    }

    @Test
    fun fetchCachedTrending_ShouldReturnFailure_WhenDatabaseQueryFails() = runTest {
        setupRepository()
        val expectedException = Exception()
        fakeRoomDbDataSource.apiError = expectedException

        val result = trendingRepository.fetchCachedTrending()

        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }

    @Test
    fun reloadTrending_ShouldReturnCorrectList_WhenNetworkAndDatabaseOperationsSucceed() = runTest {
        setupRepository()
        fakeNetworkDataSource.trendingNetworkResponseDto = SampleTrendingNetworkResponse.singleResponse
        fakeRoomDbDataSource.queryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        assertTrue(result.isSuccess)
        assertEquals(SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }, result.getOrNull())
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenApiKeyIsBlank() = runTest {
        setupRepository(giphyApiKey = "")
        val expectedException = Exception()
        fakeNetworkDataSource.apiError = expectedException

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        assertTrue(result.isFailure)
        assertFailsWith<EmptyGiphyAPIKeyException> {
            throw result.exceptionOrNull()!!
        }
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenNetworkCallFails() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        assertTrue(result.isFailure)
        assertFailsWith<Exception> {
            throw result.exceptionOrNull()!!
        }
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenDatabaseOperationThrowsException() = runTest {
        setupRepository()
        val expectedException = Exception()
        fakeRoomDbDataSource.apiError = expectedException

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }
}
