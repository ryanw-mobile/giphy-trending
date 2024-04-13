/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.model.TrendingNetworkResponse
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val giphyApiService: GiphyApi,
) : NetworkDataSource {

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): TrendingNetworkResponse {
        return giphyApiService.getTrending(
            apiKey = apiKey,
            limit = limit,
            offset = offset,
            rating = rating,
        )
    }
}
