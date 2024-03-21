package uk.ryanwong.giphytrending.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.NetworkDataSource
import uk.ryanwong.giphytrending.data.source.network.NetworkDataSourceImpl

@Module
@InstallIn(ViewModelComponent::class)
object NetworkDataSourceModule {
    @Provides
    @ViewModelScoped
    fun provideNetworkDataSource(
        giphyApiService: GiphyApi,
    ): NetworkDataSource {
        return NetworkDataSourceImpl(giphyApiService = giphyApiService)
    }
}
