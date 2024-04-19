/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.dto.SearchNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.interfaces.NetworkDataSource
import javax.inject.Inject

class RetrofitNetworkDataSource @Inject constructor(
    private val giphyApiService: GiphyApi,
) : NetworkDataSource {

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): TrendingNetworkResponseDto {
        return giphyApiService.getTrending(
            apiKey = apiKey,
            limit = limit,
            offset = offset,
            rating = rating,
        )
    }

    override suspend fun getSearch(
        apiKey: String,
        keyword: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): SearchNetworkResponseDto {
        return giphyApiService.getSearch(
            apiKey = apiKey,
            keyword = keyword,
            limit = limit,
            offset = offset,
            rating = rating,
        )
    }
}
