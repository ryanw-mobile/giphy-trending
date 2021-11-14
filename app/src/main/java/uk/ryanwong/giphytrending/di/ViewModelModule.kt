package uk.ryanwong.giphytrending.di

import dagger.Module
import dagger.Provides
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.ui.trending.TrendingViewModelFactory

@Module
class ViewModelModule {
    
    @Provides
    fun providesTrendingViewModelFactory(repository: GiphyRepository): TrendingViewModelFactory {
        return TrendingViewModelFactory(repository)
    }
}