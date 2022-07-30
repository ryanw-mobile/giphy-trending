package uk.ryanwong.giphytrending.data.source.network.model

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
data class TrendingNetworkResponse(
    @Json(name = "data")
    val trendingData: List<TrendingData>,
    val meta: Meta,
    val pagination: Pagination
)

data class TrendingData(
    val analytics_response_payload: String,
    val bitly_gif_url: String,
    val bitly_url: String,
    val content_url: String,
    val embed_url: String,
    val id: String,
    val images: Images,
    val import_datetime: Date,
    val is_sticker: Int,
    val rating: String,
    val slug: String,
    val source: String,
    val source_post_url: String,
    val source_tld: String,
    val title: String,
    val trending_datetime: Date,
    val type: String,
    val url: String,
    val username: String
)

data class Meta(
    val msg: String,
    val response_id: String,
    val status: Int
)

data class Pagination(
    val count: Int,
    val offset: Int,
    val total_count: Int
)

data class Images(
    val fixed_height: FixedHeight,
    val fixed_width: FixedWidth,
    val original: Original
)

data class FixedHeight(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
)

data class FixedWidth(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
)

data class Original(
    val frames: String,
    val hash: String,
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
)