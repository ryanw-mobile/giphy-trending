package uk.ryanwong.giphytrending.data.source.network

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ryanwong.giphytrending.model.Trending

interface GiphyApi {
    @GET("v1/gifs/trending")
    fun getTrending(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: String,
        @Query("rating") rating: String
    ): Flowable<Trending>
}