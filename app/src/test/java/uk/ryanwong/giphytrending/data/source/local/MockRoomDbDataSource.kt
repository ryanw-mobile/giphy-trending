package uk.ryanwong.giphytrending.data.source.local

class MockRoomDbDataSource : RoomDbDataSource {
    override suspend fun insertData(data: TrendingEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun queryData(): List<TrendingEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override suspend fun markDirty() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDirty() {
        TODO("Not yet implemented")
    }
}