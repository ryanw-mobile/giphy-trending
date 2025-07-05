/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import android.content.Context
import coil3.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    @Singleton
    @Provides
    fun provideCoilImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
//            .components {
//                if (android.os.Build.VERSION.SDK_INT >= 28) {
//                    add(ImageDecoderDecoder.Factory())
//                } else {
//                    add(GifDecoder.Factory())
//                }
//            }
            .build()
    }
}
