package uk.ryanwong.giphytrending.data.source.network

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ryanwong.giphytrending.data.source.network.model.TrendingNetworkResponse

interface GiphyApi {
    @GET("v1/gifs/trending")
    suspend fun getTrending(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("rating") rating: String
    ): TrendingNetworkResponse
}