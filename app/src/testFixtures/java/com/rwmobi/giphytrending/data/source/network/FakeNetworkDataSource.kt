/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.dto.MetaDto
import com.rwmobi.giphytrending.data.source.network.dto.PaginationDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource

class FakeNetworkDataSource : NetworkDataSource {
    var apiError: Throwable? = null
    var mockTrendingNetworkResponseDto: TrendingNetworkResponseDto? = null

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): TrendingNetworkResponseDto {
        apiError?.run { throw this }
        return mockTrendingNetworkResponseDto ?: TrendingNetworkResponseDto(
            metaDto = MetaDto(
                msg = "some-msg",
                responseId = "some-response-id",
                status = 0,
            ),
            trendingData = emptyList(),
            pagination = PaginationDto(
                count = 0,
                offset = 0,
                totalCount = 0,
            ),
        )
    }
}
