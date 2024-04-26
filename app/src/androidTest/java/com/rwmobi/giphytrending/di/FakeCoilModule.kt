/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.di

import android.content.Context
import coil.ImageLoader
import com.rwmobi.giphytrending.ui.test.FakeImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoilModule::class],
)
object FakeCoilModule {
    @Singleton
    @Provides
    fun provideFakeCoilImageLoader(@ApplicationContext context: Context): ImageLoader {
        return FakeImageLoader(context = context)
    }
}
