/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchNetworkResponseDto(
    @SerialName(value = "data")
    val trendingData: List<TrendingDataDto>,
    @SerialName(value = "meta")
    val metaDto: MetaDto,
    @SerialName(value = "pagination")
    val pagination: PaginationDto,
)
