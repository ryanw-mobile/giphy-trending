package uk.ryanwong.giphytrending.di

import dagger.Component
import uk.ryanwong.giphytrending.MainActivity
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.ui.settings.SettingsFragment
import uk.ryanwong.giphytrending.ui.settings.SettingsViewModel
import uk.ryanwong.giphytrending.ui.trending.TrendingFragment
import uk.ryanwong.giphytrending.ui.trending.TrendingViewModel
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        ViewModelModule::class]
)

interface AppComponent {
    fun inject(giphyRepository: GiphyRepository)
    fun inject(mainActivity: MainActivity)
    fun inject(trendingFragment: TrendingFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(trendingViewModel: TrendingViewModel)
    fun inject(settingsViewModel: SettingsViewModel)
}