/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.model

data class GifObject(
    val id: String,
    val previewUrl: String,
    val previewHeight: Int,
    val previewWidth: Int,
    val imageUrl: String,
    val webUrl: String,
    val title: String,
    val type: String,
    val username: String,
)
