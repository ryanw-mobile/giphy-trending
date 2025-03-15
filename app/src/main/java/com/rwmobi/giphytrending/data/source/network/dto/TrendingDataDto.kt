/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.dto

import com.rwmobi.giphytrending.data.source.network.CustomInstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingDataDto(
    @SerialName(value = "analytics_response_payload")
    val analyticsResponsePayload: String,
    @SerialName(value = "bitly_gif_url")
    val bitlyGifUrl: String,
    @SerialName(value = "bitly_url")
    val bitlyUrl: String,
    @SerialName(value = "content_url")
    val contentUrl: String,
    @SerialName(value = "embed_url")
    val embedUrl: String,
    val id: String,
    val images: Images,
    @SerialName(value = "import_datetime")
    @Serializable(with = CustomInstantSerializer::class)
    val importDatetime: Instant,
    @SerialName(value = "is_sticker")
    val isSticker: Int,
    val rating: String,
    val slug: String,
    val source: String,
    @SerialName(value = "source_post_url")
    val sourcePostUrl: String,
    @SerialName(value = "source_tld")
    val sourceTld: String,
    val title: String,

    @SerialName(value = "trending_datetime")
    @Serializable(with = CustomInstantSerializer::class)
    val trendingDatetime: Instant,
    val type: String,
    val url: String,
    val username: String,
)

@Serializable
data class Images(
    @SerialName(value = "fixed_height")
    val fixedHeight: FixedHeight,
    @SerialName(value = "fixed_width")
    val fixedWidth: FixedWidth,
    val original: Original,
)

@Serializable
data class FixedHeight(
    val height: String,
    val mp4: String? = null,
    @SerialName(value = "mp4_size")
    val mp4Size: String? = null,
    val size: String,
    val url: String,
    val webp: String? = null,
    @SerialName(value = "webp_size")
    val webpSize: String? = null,
    val width: String,
)

@Serializable
data class FixedWidth(
    val height: String,
    val mp4: String? = null,
    @SerialName(value = "mp4_size")
    val mp4Size: String? = null,
    val size: String,
    val url: String,
    val webp: String? = null,
    @SerialName(value = "webp_size")
    val webpSize: String? = null,
    val width: String,
)

@Serializable
data class Original(
    val frames: String,
    val hash: String,
    val height: String,
    val mp4: String? = null,
    @SerialName(value = "mp4_size")
    val mp4Size: String? = null,
    val size: String,
    val url: String,
    val webp: String? = null,
    @SerialName(value = "webp_size")
    val webpSize: String? = null,
    val width: String,
)
