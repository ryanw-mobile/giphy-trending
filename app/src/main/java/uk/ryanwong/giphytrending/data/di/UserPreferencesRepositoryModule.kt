package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepositoryImpl
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreWrapper
import uk.ryanwong.giphytrending.di.IoDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesRepositoryModule {
    @Singleton
    @Provides
    fun provideUserPreferencesRepository(
        preferencesDataStoreWrapper: PreferencesDataStoreWrapper,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            preferencesDataStoreWrapper = preferencesDataStoreWrapper,
            dispatcher = dispatcher
        )
    }
}