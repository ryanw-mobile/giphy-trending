package uk.ryanwong.giphytrending.data.source.network

import uk.ryanwong.giphytrending.BuildConfig
import uk.ryanwong.giphytrending.data.source.network.model.TrendingNetworkResponse
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val giphyApiService: GiphyApi,
) : NetworkDataSource {

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        rating: String,
    ): TrendingNetworkResponse {
        return giphyApiService.getTrending(
            BuildConfig.GIPHY_API_KEY,
            limit,
            BuildConfig.API_RATING,
        )
    }
}
