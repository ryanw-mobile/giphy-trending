/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.dto

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class SearchNetworkResponseDto(
    @Json(name = "data")
    val trendingData: List<TrendingDataDto>,
    @Json(name = "meta")
    val metaDto: MetaDto,
    @Json(name = "pagination")
    val pagination: PaginationDto,
)
