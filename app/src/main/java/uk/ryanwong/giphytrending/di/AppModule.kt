package uk.ryanwong.giphytrending.di

import dagger.Module
import dagger.Provides
import uk.ryanwong.giphytrending.data.repository.TrendingRepository
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.GiphyApiService
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel
import uk.ryanwong.giphytrending.ui.TrendingAdapter
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideApi(): GiphyApi = GiphyApiService.getClient()

    @Provides
    fun provideTrendingRepository() = TrendingRepository()

    @Provides
    fun provideTrendingList() = ArrayList<GiphyImageItemDomainModel>()

    @Provides
    fun provideTrendingAdapter(): TrendingAdapter = TrendingAdapter().apply {
        setHasStableIds(true)
    }
}