/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.dto.SearchNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource

class FakeNetworkDataSourceB : NetworkDataSource {
    var apiError: Throwable? = null
    var trendingNetworkResponseDto: TrendingNetworkResponseDto? = null
    var searchNetworkResponseDto: SearchNetworkResponseDto? = null

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): TrendingNetworkResponseDto {
        apiError?.run { throw this }
        return trendingNetworkResponseDto ?: SampleTrendingNetworkResponseDto.trendingNetworkResponseDto
    }

    override suspend fun getSearch(apiKey: String, keyword: String, limit: Int, offset: Int, rating: String): SearchNetworkResponseDto {
        apiError?.run { throw this }
        return searchNetworkResponseDto ?: SampleSearchNetworkResponseDto.searchNetworkResponseDto
    }
}
