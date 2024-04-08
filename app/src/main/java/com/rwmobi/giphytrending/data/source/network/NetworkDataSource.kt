/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.model.TrendingNetworkResponse

interface NetworkDataSource {
    suspend fun getTrending(apiKey: String, limit: Int, rating: String): TrendingNetworkResponse
}
