/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trending")
data class TrendingEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "preview_url")
    val previewUrl: String,
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
    val trendingDateTime: Date,
    @ColumnInfo(name = "import_datetime")
    val importDateTime: Date,

    // This field is not from the RestAPI
    // Every time we overwrite the DB with API data. To remove outdated data in the FB,
    // we use this extra dirty bit approach to simulate synchronization.
    @ColumnInfo(name = "dirty")
    val dirty: Boolean = false,
)
