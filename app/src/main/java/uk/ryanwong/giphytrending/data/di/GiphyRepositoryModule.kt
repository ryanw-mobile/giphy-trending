package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.GiphyRepositoryImpl
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepositoryImpl
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GiphyRepositoryModule {
    @Singleton
    @Provides
    fun provideGiphyRepository(preferenceDataStoreManager: PreferencesDataStoreManager): GiphyRepository {
        return GiphyRepositoryImpl()
    }
}
