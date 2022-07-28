package uk.ryanwong.giphytrending.data.source.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.NetworkDataSource
import uk.ryanwong.giphytrending.data.source.network.NetworkDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataSourceModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(
        giphyApiService: GiphyApi
    ): NetworkDataSource {
        return NetworkDataSourceImpl(giphyApiService = giphyApiService)
    }
}