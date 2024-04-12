/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import com.rwmobi.giphytrending.data.source.network.model.TrendingData
import com.rwmobi.giphytrending.domain.model.GiphyImageItem

fun TrendingEntity.toDomainModel() = GiphyImageItem(
    id = this.id,
    previewUrl = this.previewUrl,
    imageUrl = this.imageUrl,
    webUrl = this.webUrl,
    title = this.title,
    type = this.type,
    username = this.username,
)

fun List<TrendingEntity>.toDomainModelList() = this.map { it.toDomainModel() }

fun TrendingData.toTrendingEntity() = TrendingEntity(
    id = this.id,
    previewUrl = urlCleanUp(this.images.fixedWidth.url),
    imageUrl = urlCleanUp(this.images.original.url),
    webUrl = this.url,
    title = this.title,
    type = this.type,
    username = this.username,
    trendingDateTime = this.trendingDatetime,
    importDateTime = this.importDatetime,
)

fun List<TrendingData>.toTrendingEntityList() = this.map { it.toTrendingEntity() }

/**
 * The image URL returned by the server contains tracking code.
 * Trying to remove it to avoid unnecessary cache invalidation (experimental)
 */
private fun urlCleanUp(url: String): String {
    return url.substringBefore("?")
}
