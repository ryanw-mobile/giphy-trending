/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource

class FakeNetworkDataSourceB : NetworkDataSource {
    var apiError: Throwable? = null
    var mockTrendingNetworkResponseDto: TrendingNetworkResponseDto? = null

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): TrendingNetworkResponseDto {
        apiError?.run { throw this }
        return mockTrendingNetworkResponseDto ?: SampleTrendingNetworkResponseDto.trendingNetworkResponseDto
    }
}
