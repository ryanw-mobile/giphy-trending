package uk.ryanwong.giphytrending.data.source.network

import uk.ryanwong.giphytrending.data.source.network.model.TrendingNetworkResponse

interface NetworkDataSource {
    suspend fun getTrending(apiKey: String, limit: Int, rating: String): TrendingNetworkResponse
}