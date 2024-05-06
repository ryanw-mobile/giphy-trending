/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local.mappers

import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import com.rwmobi.giphytrending.data.source.network.dto.TrendingDataDto

fun TrendingDataDto.toTrendingEntity() = TrendingEntity(
    id = this.id,
    previewUrl = urlCleanUp(this.images.fixedWidth.url),
    previewHeight = this.images.fixedWidth.height.toInt(),
    previewWidth = this.images.fixedWidth.width.toInt(),
    imageUrl = urlCleanUp(this.images.original.url),
    webUrl = this.url,
    title = this.title,
    type = this.type,
    username = this.username,
    trendingDateTime = this.trendingDatetime,
    importDateTime = this.importDatetime,
)

fun List<TrendingDataDto>.toTrendingEntity() = this.map { it.toTrendingEntity() }

/**
 * The image URL returned by the server contains tracking code.
 * Trying to remove it to avoid unnecessary cache invalidation (experimental)
 */
private fun urlCleanUp(url: String): String {
    return url.substringBefore("?")
}
