/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.dto

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class MetaDto(
    val msg: String,
    @Json(name = "response_id")
    val responseId: String,
    val status: Int,
)
