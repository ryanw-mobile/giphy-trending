package uk.ryanwong.giphytrending.data.repository

class MockUserPreferencesRepository : UserPreferencesRepository {

    var mockApiMaxResponse: Int = -1
    override suspend fun setApiMax(apiMax: Int) {
        mockApiMaxResponse = apiMax
    }

    var mockGetApiMaxResponse: Result<Int>? = null
    override suspend fun getApiMax(): Result<Int> {
        return mockGetApiMaxResponse ?: Result.success(mockApiMaxResponse)
    }
}