package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.source.local.mappers.asGiphyImageItem
import com.rwmobi.giphytrending.data.source.local.mappers.asTrendingEntity
import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSource
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import com.rwmobi.giphytrending.test.testdata.SampleSearchNetworkResponse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchRepositoryImplTest {
    private lateinit var searchRepository: SearchRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    private fun setupRepository(giphyApiKey: String = "some-api-key") {
        dispatcher = UnconfinedTestDispatcher()
        fakeNetworkDataSource = FakeNetworkDataSource()
        searchRepository = SearchRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }

    @Test
    fun `search should return empty result if keyword is null`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword: String? = null

        val result = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `search should return empty result if keyword is empty`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword: String? = ""

        val result = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `search should return empty result if keyword is blank`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "     "

        val result = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `search should return result if API request was successful`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"

        val result = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleSearchNetworkResponse.singleResponse.trendingData.asTrendingEntity().asGiphyImageItem()
    }

    @Test
    fun `search should return failure if giphyApiKey is blank`() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse

        val result = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe EmptyGiphyAPIKeyException()
    }

    @Test
    fun `search should return failure if network call returns an error`() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val result = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }
}
