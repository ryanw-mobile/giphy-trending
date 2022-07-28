package uk.ryanwong.giphytrending.data.source.local

interface RoomDbDataSource {
    suspend fun insertData(data: TrendingEntity)
    suspend fun insertAllData(data: List<TrendingEntity>)
    suspend fun queryData(): List<TrendingEntity>
    suspend fun clear()
    suspend fun markDirty()
    suspend fun deleteDirty()
}