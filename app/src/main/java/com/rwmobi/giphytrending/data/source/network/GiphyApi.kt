/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.dto.SearchNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

// https://developers.giphy.com/docs/api/endpoint#trending

interface GiphyApi {
    @GET("v1/gifs/trending")
    suspend fun getTrending(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String,
    ): TrendingNetworkResponseDto

    @GET("v1/gifs/search")
    suspend fun getSearch(
        @Query("api_key") apiKey: String,
        @Query("q") keyword: String,
        @Query("limit") limit: Int, // ⚠️ observe API limit
        @Query("offset") offset: Int,
        @Query("rating") rating: String,
    ): SearchNetworkResponseDto
}
