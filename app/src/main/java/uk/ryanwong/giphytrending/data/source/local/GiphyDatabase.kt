package uk.ryanwong.giphytrending.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TrendingEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GiphyDatabase : RoomDatabase() {
    abstract fun trendingDao(): TrendingDao
}