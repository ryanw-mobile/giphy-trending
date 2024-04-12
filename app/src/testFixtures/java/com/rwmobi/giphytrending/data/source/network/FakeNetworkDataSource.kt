package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.model.Meta
import com.rwmobi.giphytrending.data.source.network.model.Pagination
import com.rwmobi.giphytrending.data.source.network.model.TrendingNetworkResponse

class FakeNetworkDataSource : NetworkDataSource {
    var apiError: Throwable? = null
    var mockTrendingNetworkResponse: TrendingNetworkResponse? = null

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        rating: String,
    ): TrendingNetworkResponse {
        apiError?.run { throw this }
        return mockTrendingNetworkResponse ?: TrendingNetworkResponse(
            meta = Meta(
                msg = "some-msg",
                responseId = "some-response-id",
                status = 0,
            ),
            trendingData = emptyList(),
            pagination = Pagination(
                count = 0,
                offset = 0,
                totalCount = 0,
            ),
        )
    }
}
