package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.GiphyApiService

@Module
@InstallIn(ViewModelComponent::class)
object GiphyApiServiceModule {
    @ViewModelScoped
    @Provides
    fun provideApi(): GiphyApi {
        return GiphyApiService.getClient()
    }
}
