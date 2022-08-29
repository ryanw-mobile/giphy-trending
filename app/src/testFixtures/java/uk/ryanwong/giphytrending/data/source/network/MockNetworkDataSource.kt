package uk.ryanwong.giphytrending.data.source.network

import uk.ryanwong.giphytrending.data.source.network.model.Meta
import uk.ryanwong.giphytrending.data.source.network.model.Pagination
import uk.ryanwong.giphytrending.data.source.network.model.TrendingNetworkResponse

class MockNetworkDataSource : NetworkDataSource {
    var apiError: Throwable? = null
    var mockTrendingNetworkResponse: TrendingNetworkResponse? = null

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        rating: String
    ): TrendingNetworkResponse {
        apiError?.run { throw this }
        return mockTrendingNetworkResponse ?: TrendingNetworkResponse(
            meta = Meta(
                msg = "some-msg",
                response_id = "some-response-id",
                status = 0
            ),
            trendingData = emptyList(),
            pagination = Pagination(
                count = 0,
                offset = 0,
                total_count = 0
            )
        )
    }
}
