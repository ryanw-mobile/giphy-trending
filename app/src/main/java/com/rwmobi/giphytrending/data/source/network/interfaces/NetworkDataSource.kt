/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.interfaces

import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto

interface NetworkDataSource {
    suspend fun getTrending(apiKey: String, limit: Int, offset: Int, rating: String): TrendingNetworkResponseDto
}
