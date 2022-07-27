package uk.ryanwong.giphytrending.data.source.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.ui.GiphyImageItemAdapter

@Module
@InstallIn(SingletonComponent::class)
object TrendingAdapterModule {
    @Provides
    fun provideTrendingAdapter(): GiphyImageItemAdapter {
        return GiphyImageItemAdapter().apply {
            setHasStableIds(true)
        }
    }
}