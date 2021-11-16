package uk.ryanwong.giphytrending.di

import dagger.Module
import dagger.Provides
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import uk.ryanwong.giphytrending.ui.settings.SettingsViewModelFactory
import uk.ryanwong.giphytrending.ui.trending.TrendingViewModelFactory

@Module
class ViewModelModule {

    @Provides
    fun providesTrendingViewModelFactory(
        giphyRepository: GiphyRepository,
        userPreferencesRepository: UserPreferencesRepository
    ): TrendingViewModelFactory {
        return TrendingViewModelFactory(giphyRepository, userPreferencesRepository)
    }

    @Provides
    fun providesSettingsViewModelFactory(repository: UserPreferencesRepository): SettingsViewModelFactory {
        return SettingsViewModelFactory(repository)
    }
}