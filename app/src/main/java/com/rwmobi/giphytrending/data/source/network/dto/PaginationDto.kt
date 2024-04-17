/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.dto

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class PaginationDto(
    val count: Int,
    val offset: Int,
    @Json(name = "total_count")
    val totalCount: Int,
)
