/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "trending")
data class TrendingEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "preview_url")
    val previewUrl: String,
    @ColumnInfo(name = "preview_height")
    val previewHeight: Int,
    @ColumnInfo(name = "preview_width")
    val previewWidth: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "web_url")
    val webUrl: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "trending_datetime")
    val trendingDateTime: Instant,
    @ColumnInfo(name = "import_datetime")
    val importDateTime: Instant,

    // This field is not from the RestAPI
    // Every time we overwrite the DB with API data. To remove outdated data in the FB,
    // we use this extra dirty bit approach to simulate synchronization.
    @ColumnInfo(name = "dirty")
    val dirty: Boolean = false,
)
