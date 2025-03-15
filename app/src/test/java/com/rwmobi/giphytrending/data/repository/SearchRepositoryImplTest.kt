/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository

import com.rwmobi.giphytrending.data.repository.mappers.toGifObject
import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSource
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import com.rwmobi.giphytrending.test.testdata.SampleSearchNetworkResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class SearchRepositoryImplTest {
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

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun search_WithNullKeyword_ShouldReturnEmptyResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword: String? = null

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        assertTrue(searchResult.isSuccess)
        assertEquals(emptyList(), searchResult.getOrNull())
    }

    @Test
    fun search_WithEmptyKeyword_ShouldReturnEmptyResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword: String? = ""

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        assertTrue(searchResult.isSuccess)
        assertEquals(emptyList(), searchResult.getOrNull())
    }

    @Test
    fun search_WithBlankKeyword_ShouldReturnEmptyResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "     "

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        assertTrue(searchResult.isSuccess)
        assertEquals(emptyList(), searchResult.getOrNull())
    }

    @Test
    fun search_WithValidKeyword_ShouldReturnSuccessfulResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"
        val expectedSearchResult = SampleSearchNetworkResponse.singleResponse.trendingData.map { it.toGifObject() }

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        assertTrue(searchResult.isSuccess)
        assertEquals(expectedSearchResult, searchResult.getOrNull())
    }

    @Test
    fun search_WithBlankApiKey_ShouldThrowEmptyGiphyAPIKeyException() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse

        val searchResult = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        assertTrue(searchResult.isFailure)
        assertFailsWith<EmptyGiphyAPIKeyException> {
            throw searchResult.exceptionOrNull()!!
        }
    }

    @Test
    fun search_WithNetworkError_ShouldReturnFailure() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val searchResult = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        assertTrue(searchResult.isFailure)
        assertFailsWith<Exception> {
            throw searchResult.exceptionOrNull()!!
        }
    }

    // lastSuccessfulSearchResults
    @Test
    fun getLastSuccessfulSearchKeyword_WithoutSuccessfulSearch_ShouldReturnNull() {
        setupRepository()
        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        assertNull(lastSuccessfulSearchKeyword)
    }

    @Test
    fun getLastSuccessfulSearchResults_WithoutSuccessfulSearch_ShouldReturnNull() {
        setupRepository()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        assertNull(lastSuccessfulSearchResults)
    }

    @Test
    fun getLastSuccessfulSearch_WithSuccessfulSearch_ShouldReturnKeywordAndResults() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"
        val expectedSearchResults = SampleSearchNetworkResponse.singleResponse.trendingData.map { it.toGifObject() }
        searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        assertEquals(keyword, lastSuccessfulSearchKeyword)
        assertEquals(expectedSearchResults, lastSuccessfulSearchResults)
    }

    @Test
    fun getLastSuccessfulSearch_AfterFailedSearch_ShouldMaintainPreviousSuccess() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"
        val expectedSearchResults = SampleSearchNetworkResponse.singleResponse.trendingData.map { it.toGifObject() }
        searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)
        fakeNetworkDataSource.apiError = Exception()
        searchRepository.search(keyword = "failed keyword", limit = 100, rating = Rating.R)

        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        assertEquals(keyword, lastSuccessfulSearchKeyword)
        assertEquals(expectedSearchResults, lastSuccessfulSearchResults)
    }
}
