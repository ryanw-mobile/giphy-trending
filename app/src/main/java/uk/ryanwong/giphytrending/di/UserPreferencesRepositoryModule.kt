package uk.ryanwong.giphytrending.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepositoryImpl
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreWrapper
import uk.ryanwong.giphytrending.domain.repository.UserPreferencesRepository

@Module
@InstallIn(ViewModelComponent::class)
object UserPreferencesRepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideUserPreferencesRepository(
        preferencesDataStoreWrapper: PreferencesDataStoreWrapper,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            preferencesDataStoreWrapper = preferencesDataStoreWrapper,
            dispatcher = dispatcher,
        )
    }
}
