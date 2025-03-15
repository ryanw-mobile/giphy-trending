/*
 * Copyright (c) 2024-2025. Ryan Wong
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
internal class TrendingRepositoryImplTest {
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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `returns correct list when database query succeeds`() = runTest {
        setupRepository()
        fakeRoomDbDataSource.queryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = trendingRepository.fetchCachedTrending()

        assertTrue(result.isSuccess)
        assertEquals(SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }, result.getOrNull())
    }

    @Test
    fun `returns failure when database query fails`() = runTest {
        setupRepository()
        val expectedException = Exception()
        fakeRoomDbDataSource.apiError = expectedException

        val result = trendingRepository.fetchCachedTrending()

        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }

    @Test
    fun `returns correct list when network and database operations succeed`() = runTest {
        setupRepository()
        fakeNetworkDataSource.trendingNetworkResponseDto = SampleTrendingNetworkResponse.singleResponse
        fakeRoomDbDataSource.queryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        assertTrue(result.isSuccess)
        assertEquals(SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }, result.getOrNull())
    }

    @Test
    fun `throws EmptyGiphyAPIKeyException when API key is blank`() = runTest {
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
    fun `returns failure when network call fails`() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        assertTrue(result.isFailure)
        assertFailsWith<Exception> {
            throw result.exceptionOrNull()!!
        }
    }

    @Test
    fun `returns failure when database operation throws exception`() = runTest {
        setupRepository()
        val expectedException = Exception()
        fakeRoomDbDataSource.apiError = expectedException

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }
}
