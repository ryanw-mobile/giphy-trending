/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository.mappers

import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import com.rwmobi.giphytrending.data.source.network.dto.TrendingDataDto
import com.rwmobi.giphytrending.domain.model.GifObject

fun TrendingDataDto.toEntity() = TrendingEntity(
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

fun TrendingDataDto.toGifObject() = GifObject(
    id = this.id,
    previewUrl = urlCleanUp(this.images.fixedWidth.url),
    previewHeight = this.images.fixedWidth.height.toInt(),
    previewWidth = this.images.fixedWidth.width.toInt(),
    imageUrl = urlCleanUp(this.images.original.url),
    webUrl = this.url,
    title = this.title,
    type = this.type,
    username = this.username,
)

/**
 * The image URL returned by the server contains tracking code.
 * Trying to remove it to avoid unnecessary cache invalidation (experimental)
 */
private fun urlCleanUp(url: String): String {
    return url.substringBefore("?")
}
