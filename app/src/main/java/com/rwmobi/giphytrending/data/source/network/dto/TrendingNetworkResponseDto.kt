/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.dto

import androidx.annotation.Keep
import com.squareup.moshi.Json

/***
 * Note: Obviously large chunk of data is not being used by this App for the moment,
 * and can be removed. However this is a demo app which I do not have a firmed scope here,
 * leaving it there might help me quickly get new ideas on what else to app to this App.
 *
 * Otherwise in a real project once we know what's required, we don't have to keep so much,
 * as they will be thrown away at the time we convert it to domain model anyway.
 */
@Keep
data class TrendingNetworkResponseDto(
    @Json(name = "data")
    val trendingData: List<TrendingDataDto>,
    @Json(name = "meta")
    val metaDto: MetaDto,
    @Json(name = "pagination")
    val pagination: PaginationDto,
)
