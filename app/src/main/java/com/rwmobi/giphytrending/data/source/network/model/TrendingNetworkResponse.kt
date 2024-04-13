/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.util.Date

/***
 * Note: Obviously large chunk of data is not being used by this App for the moment,
 * and can be removed. However this is a demo app which I do not have a firmed scope here,
 * leaving it there might help me quickly get new ideas on what else to app to this App.
 *
 * Otherwise in a real project once we know what's required, we don't have to keep so much,
 * as they will be thrown away at the time we convert it to domain model anyway.
 */
@Keep
data class TrendingNetworkResponse(
    @Json(name = "data")
    val trendingData: List<TrendingData>,
    val meta: Meta,
    val pagination: Pagination,
)

@Keep
data class TrendingData(
    @Json(name = "analytics_response_payload")
    val analyticsResponsePayload: String,
    @Json(name = "bitly_gif_url")
    val bitlyGifUrl: String,
    @Json(name = "bitly_url")
    val bitlyUrl: String,
    @Json(name = "content_url")
    val contentUrl: String,
    @Json(name = "embed_url")
    val embedUrl: String,
    val id: String,
    val images: Images,
    @Json(name = "import_datetime")
    val importDatetime: Date,
    @Json(name = "is_sticker")
    val isSticker: Int,
    val rating: String,
    val slug: String,
    val source: String,
    @Json(name = "source_post_url")
    val sourcePostUrl: String,
    @Json(name = "source_tld")
    val sourceTld: String,
    val title: String,
    @Json(name = "trending_datetime")
    val trendingDatetime: Date,
    val type: String,
    val url: String,
    val username: String,
)

@Keep
data class Meta(
    val msg: String,
    @Json(name = "response_id")
    val responseId: String,
    val status: Int,
)

@Keep
data class Pagination(
    val count: Int,
    val offset: Int,
    @Json(name = "total_count")
    val totalCount: Int,
)

@Keep
data class Images(
    @Json(name = "fixed_height")
    val fixedHeight: FixedHeight,
    @Json(name = "fixed_width")
    val fixedWidth: FixedWidth,
    val original: Original,
)

@Keep
data class FixedHeight(
    val height: String,
    val mp4: String? = null,
    @Json(name = "mp4_size")
    val mp4Size: String? = null,
    val size: String,
    val url: String,
    val webp: String? = null,
    @Json(name = "webp_size")
    val webpSize: String? = null,
    val width: String,
)

@Keep
data class FixedWidth(
    val height: String,
    val mp4: String? = null,
    @Json(name = "mp4_size")
    val mp4Size: String? = null,
    val size: String,
    val url: String,
    val webp: String? = null,
    @Json(name = "webp_size")
    val webpSize: String? = null,
    val width: String,
)

@Keep
data class Original(
    val frames: String,
    val hash: String,
    val height: String,
    val mp4: String? = null,
    @Json(name = "mp4_size")
    val mp4Size: String? = null,
    val size: String,
    val url: String,
    val webp: String? = null,
    @Json(name = "webp_size")
    val webpSize: String? = null,
    val width: String,
)
