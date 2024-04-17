/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.source.local.FakeDatabaseDataSource
import com.rwmobi.giphytrending.data.source.local.mappers.asGiphyImageItem
import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSource
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntityList
import com.rwmobi.giphytrending.test.testdata.SampleTrendingNetworkResponse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GiphyRepositoryImplTest {
    private lateinit var giphyRepository: GiphyRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeRoomDbDataSource: FakeDatabaseDataSource
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    private fun setupRepository(giphyApiKey: String = "some-api-key") {
        dispatcher = UnconfinedTestDispatcher()
        fakeRoomDbDataSource = FakeDatabaseDataSource()
        fakeNetworkDataSource = FakeNetworkDataSource()
        giphyRepository = GiphyRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            databaseDataSource = fakeRoomDbDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }

    @Test
    fun `fetchCachedTrending should return correct list if database query success`() = runTest {
        setupRepository()
        fakeRoomDbDataSource.mockQueryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = giphyRepository.fetchCachedTrending()

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.singleEntityList.asGiphyImageItem()
    }

    @Test
    fun `fetchCachedTrending should return failure if database query throws exception`() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = giphyRepository.fetchCachedTrending()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun `reloadTrending should return correct list if network and database operations all success`() = runTest {
        setupRepository()
        fakeNetworkDataSource.mockTrendingNetworkResponseDto = SampleTrendingNetworkResponse.singleResponse
        fakeRoomDbDataSource.mockQueryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.singleEntityList.asGiphyImageItem()
    }

    @Test
    fun `reloadTrending should return failure if giphyApiKey is blank`() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.apiError = Exception()

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe EmptyGiphyAPIKeyException()
    }

    @Test
    fun `reloadTrending should return failure if network call returns an error`() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun `reloadTrending should return failure if database operation throws exception`() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }
}
