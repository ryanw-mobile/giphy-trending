package uk.ryanwong.giphytrending.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ryanwong.giphytrending.BuildConfig

@Database(entities = [TrendingEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GiphyDatabase : RoomDatabase() {
    abstract fun trendingDao(): TrendingDao

    companion object {

        @Volatile // All threads have immediate access to this property
        private var instance: GiphyDatabase? = null

        private val LOCK = Any() // Makes sure no threads making the same thing at the same time

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GiphyDatabase::class.java,
                BuildConfig.DATABASE_NAME
            ).fallbackToDestructiveMigration().build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}