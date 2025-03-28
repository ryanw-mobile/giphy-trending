/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.repository.mappers

import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import com.rwmobi.giphytrending.domain.model.GifObject

fun TrendingEntity.toGifObject() = GifObject(
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
