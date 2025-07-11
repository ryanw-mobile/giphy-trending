/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.data.source.network.dto.SearchNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KtorNetworkDataSource(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : NetworkDataSource {
    private val baseUrl = BuildConfig.GIPHY_ENDPOINT
    private val trendingUrl = "${baseUrl}v1/gifs/trending"
    private val searchURl = "${baseUrl}v1/gifs/search"

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): TrendingNetworkResponseDto = withContext(dispatcher) {
        httpClient.get(trendingUrl) {
            parameter("api_key", apiKey)
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("rating", rating)
        }.body()
    }

    override suspend fun getSearch(
        apiKey: String,
        keyword: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): SearchNetworkResponseDto = withContext(dispatcher) {
        httpClient.get(searchURl) {
            parameter("api_key", apiKey)
            parameter("q", keyword)
            parameter("limit", limit) // ⚠️ observe API limit
            parameter("offset", offset)
            parameter("rating", rating)
        }.body()
    }
}
