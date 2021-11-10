package uk.ryanwong.giphytrending.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface TrendingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: TrendingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllData(data: List<TrendingEntity>)

    @Query("SELECT * FROM trending ORDER BY trending_datetime DESC")
    fun queryData(): Single<List<TrendingEntity>>

    @Query("DELETE FROM trending")
    fun clear()
}