/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.data.source.network.model.TrendingNetworkResponse
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val giphyApiService: GiphyApi,
) : NetworkDataSource {

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        rating: String,
    ): TrendingNetworkResponse {
        return giphyApiService.getTrending(
            BuildConfig.GIPHY_API_KEY,
            limit,
            BuildConfig.API_RATING,
        )
    }
}
