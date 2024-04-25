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

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `search should return empty result if keyword is empty`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword: String? = ""

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `search should return empty result if keyword is blank`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "     "

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `search should return result if API request was successful`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe SampleSearchNetworkResponse.singleResponse.trendingData.asTrendingEntity().asGiphyImageItem()
    }

    @Test
    fun `search should return failure if giphyApiKey is blank`() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse

        val searchResult = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        searchResult.isFailure shouldBe true
        searchResult.exceptionOrNull() shouldBe EmptyGiphyAPIKeyException()
    }

    @Test
    fun `search should return failure if network call returns an error`() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val searchResult = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        searchResult.isFailure shouldBe true
        searchResult.exceptionOrNull() shouldBe Exception()
    }

    // lastSuccessfulSearchResults
    @Test
    fun `getLastSuccessfulSearchKeyword should return null if no successful search was done`() {
        setupRepository()
        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        lastSuccessfulSearchKeyword shouldBe null
    }

    @Test
    fun `getLastSuccessfulSearchResults should return null if no successful search was done`() {
        setupRepository()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        lastSuccessfulSearchResults shouldBe null
    }

    @Test
    fun `should return last search keyword and results if a successful search was done`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"
        searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        lastSuccessfulSearchKeyword shouldBe keyword
        lastSuccessfulSearchResults shouldBe SampleSearchNetworkResponse.singleResponse.trendingData.asTrendingEntity().asGiphyImageItem()
    }

    @Test
    fun `should return last successful search keyword and results if the recent search was failure`() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"
        searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)
        fakeNetworkDataSource.apiError = Exception()
        searchRepository.search(keyword = "failed keyword", limit = 100, rating = Rating.R)

        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        lastSuccessfulSearchKeyword shouldBe keyword
        lastSuccessfulSearchResults shouldBe SampleSearchNetworkResponse.singleResponse.trendingData.asTrendingEntity().asGiphyImageItem()
    }
}
