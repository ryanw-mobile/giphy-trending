package uk.ryanwong.giphytrending.di

import dagger.Component
import uk.ryanwong.giphytrending.MainActivity
import uk.ryanwong.giphytrending.data.repository.TrendingRepository
import uk.ryanwong.giphytrending.ui.trending.TrendingFragment
import uk.ryanwong.giphytrending.ui.trending.TrendingViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(trendingRepository: TrendingRepository)
    fun inject(viewModel: TrendingViewModel)
    fun inject(mainActivity: MainActivity)
    fun inject(trendingFragment: TrendingFragment)
}