package uk.ryanwong.giphytrending.data.source.local

class MockRoomDbDataSource : RoomDbDataSource {
    override suspend fun insertData(data: TrendingEntity) {
        mockQueryDataResponse = listOf(data)
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        mockQueryDataResponse = data
    }

    var mockQueryDataResponse: List<TrendingEntity>? = null
    override suspend fun queryData(): List<TrendingEntity> {
        return mockQueryDataResponse ?: emptyList()
    }

    override suspend fun clear() {
        mockQueryDataResponse = emptyList()
    }

    override suspend fun markDirty() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDirty() {
        TODO("Not yet implemented")
    }
}