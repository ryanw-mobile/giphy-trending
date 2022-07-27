package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepositoryImpl
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserPreferencesRepositoryModule {
    @Singleton
    @Provides
    fun provideUserPreferencesRepository(preferenceDataStoreManager: PreferencesDataStoreManager): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(preferencesDataStoreManager = preferenceDataStoreManager)
    }
}
