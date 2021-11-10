package uk.ryanwong.giphytrending.data.source.network.model

import com.squareup.moshi.Json
import java.util.*

data class TrendingNetworkModel(
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
    val `480w_still`: WStill,
    val downsized: Downsized,
    val downsized_large: DownsizedLarge,
    val downsized_medium: DownsizedMedium,
    val downsized_small: DownsizedSmall,
    val downsized_still: DownsizedStill,
    val fixed_height: FixedHeight,
    val fixed_height_downsampled: FixedHeightDownsampled,
    val fixed_height_small: FixedHeightSmall,
    val fixed_height_small_still: FixedHeightSmallStill,
    val fixed_height_still: FixedHeightStill,
    val fixed_width: FixedWidth,
    val fixed_width_downsampled: FixedWidthDownsampled,
    val fixed_width_small: FixedWidthSmall,
    val fixed_width_small_still: FixedWidthSmallStill,
    val fixed_width_still: FixedWidthStill,
    val looping: Looping,
    val original: Original,
    val original_mp4: OriginalMp4,
    val original_still: OriginalStill,
    val preview: Preview,
    val preview_gif: PreviewGif,
    val preview_webp: PreviewWebp
)

data class WStill(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class Downsized(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class DownsizedLarge(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class DownsizedMedium(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class DownsizedSmall(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val width: String
)

data class DownsizedStill(
    val height: String,
    val size: String,
    val url: String,
    val width: String
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

data class FixedHeightDownsampled(
    val height: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
)

data class FixedHeightSmall(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
)

data class FixedHeightSmallStill(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class FixedHeightStill(
    val height: String,
    val size: String,
    val url: String,
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

data class FixedWidthDownsampled(
    val height: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
)

data class FixedWidthSmall(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
)

data class FixedWidthSmallStill(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class FixedWidthStill(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class Hd(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val width: String
)

data class Looping(
    val mp4: String,
    val mp4_size: String
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

data class OriginalMp4(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val width: String
)

data class OriginalStill(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class Preview(
    val height: String,
    val mp4: String,
    val mp4_size: String,
    val width: String
)

data class PreviewGif(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)

data class PreviewWebp(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)