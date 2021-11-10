package uk.ryanwong.giphytrending.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.ryanwong.giphytrending.BuildConfig

@Database(entities = [DataEntity::class], version = 1)
abstract class TrendingDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao

    companion object {

        @Volatile // All threads have immediate access to this property
        private var instance: TrendingDatabase? = null

        private val LOCK = Any() // Makes sure no threads making the same thing at the same time

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TrendingDatabase::class.java,
                BuildConfig.DATABASE_NAME
            ).fallbackToDestructiveMigration().build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}