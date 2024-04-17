/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local.mappers

import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import com.rwmobi.giphytrending.domain.model.GiphyImageItem

fun TrendingEntity.asGiphyImageItem() = GiphyImageItem(
    id = this.id,
    previewUrl = this.previewUrl,
    previewHeight = this.previewHeight,
    previewWidth = this.previewWidth,
    imageUrl = this.imageUrl,
    webUrl = this.webUrl,
    title = this.title,
    type = this.type,
    username = this.username,
)

fun List<TrendingEntity>.asGiphyImageItem() = this.map { it.asGiphyImageItem() }
