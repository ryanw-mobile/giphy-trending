package uk.ryanwong.giphytrending.data.source.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.ryanwong.giphytrending.BuildConfig
import java.util.concurrent.TimeUnit

object GiphyApiService {
    private fun createOkHttpClient(): OkHttpClient {
        /***
         * Remark: For development purpose we can use the Profiler on Android Studio to inspect the
         * HTTP traffic, which is more detailed than using HttpLoggingInterceptor.
         */
        // val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            //    .addInterceptor(logger)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    fun getClient(): GiphyApi {
        return Retrofit.Builder()
            .client(createOkHttpClient())
            .baseUrl(BuildConfig.GIPHY_ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GiphyApi::class.java)
    }
}