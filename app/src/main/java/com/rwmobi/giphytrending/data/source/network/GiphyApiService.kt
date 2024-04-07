/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
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

    // AppModule.provideApi() helps inject this
    fun getClient(): GiphyApi {
        val moshi = Moshi.Builder()
            .add(Date::class.java, CustomDateTimeAdapter("yyyy-MM-dd HH:mm:ss").nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .client(createOkHttpClient())
            .baseUrl(BuildConfig.GIPHY_ENDPOINT)
            //      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GiphyApi::class.java)
    }
}
