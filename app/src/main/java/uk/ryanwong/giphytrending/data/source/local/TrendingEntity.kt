package uk.ryanwong.giphytrending.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "trending")
data class TrendingEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "trending_datetime")
    val trendingDateTime: Date
)