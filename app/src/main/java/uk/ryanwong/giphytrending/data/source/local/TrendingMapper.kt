package uk.ryanwong.giphytrending.data.source.local

import uk.ryanwong.giphytrending.data.source.network.model.TrendingData
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel

fun TrendingEntity.toDomainModel() = GiphyImageItemDomainModel(
    id = this.id,
    previewUrl = this.previewUrl,
    imageUrl = this.imageUrl,
    webUrl = this.webUrl,
    title = this.title,
    type = this.type,
    username = this.username
)

fun List<TrendingEntity>.toDomainModelList() = this.map { it.toDomainModel() }

fun TrendingData.toTrendingEntity() = TrendingEntity(
    id = this.id,
    previewUrl = urlCleanUp(this.images.fixed_width.url),
    imageUrl = urlCleanUp(this.images.original.url),
    webUrl = this.url,
    title = this.title,
    type = this.type,
    username = this.username,
    trendingDateTime = this.trending_datetime,
    importDateTime = this.import_datetime
)

fun List<TrendingData>.toTrendingEntityList() = this.map { it.toTrendingEntity() }

/**
 * The image URL returned by the server contains tracking code.
 * Trying to remove it to avoid unnecessary cache invalidation (experimental)
 */
private fun urlCleanUp(url: String): String {
    return url.substringBefore("?")
}
