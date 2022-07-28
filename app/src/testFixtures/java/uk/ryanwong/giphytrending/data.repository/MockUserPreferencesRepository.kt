package uk.ryanwong.giphytrending.data.repository

class MockUserPreferencesRepository : UserPreferencesRepository {

    private var mockApiMaxResponse = -1
    var mockSetApiMaxResponse: Result<Unit>? = null
    override suspend fun setApiMax(apiMax: Int): Result<Unit> {
        mockApiMaxResponse = apiMax
        return mockSetApiMaxResponse ?: Result.success(Unit)
    }

    var mockGetApiMaxResponse: Result<Int>? = null
    override suspend fun getApiMax(): Result<Int> {
        return mockGetApiMaxResponse ?: Result.success(mockApiMaxResponse)
    }
}