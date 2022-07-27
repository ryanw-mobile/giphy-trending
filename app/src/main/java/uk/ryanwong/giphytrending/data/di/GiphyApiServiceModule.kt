package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.GiphyApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GiphyApiServiceModule {
    @Singleton
    @Provides
    fun provideApi(): GiphyApi {
        return GiphyApiService.getClient()
    }
}