/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/***
 * Note: Obviously large chunk of data is not being used by this App for the moment,
 * and can be removed. However this is a demo app which I do not have a firmed scope here,
 * leaving it there might help me quickly get new ideas on what else to app to this App.
 *
 * Otherwise in a real project once we know what's required, we don't have to keep so much,
 * as they will be thrown away at the time we convert it to domain model anyway.
 */
@Serializable
data class TrendingNetworkResponseDto(
    @SerialName(value = "data")
    val trendingData: List<TrendingDataDto>,
    @SerialName(value = "meta")
    val metaDto: MetaDto,
    @SerialName(value = "pagination")
    val pagination: PaginationDto,
)
