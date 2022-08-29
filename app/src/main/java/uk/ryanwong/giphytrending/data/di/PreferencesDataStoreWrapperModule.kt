package uk.ryanwong.giphytrending.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreWrapper
import uk.ryanwong.giphytrending.data.source.preferences.PreferencesDataStoreWrapperImpl

private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

@Module
@InstallIn(ViewModelComponent::class)
object PreferencesDataStoreWrapperModule {

    @ViewModelScoped
    @Provides
    fun provideDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.userDataStore
    }

    @ViewModelScoped
    @Provides
    fun providePreferenceDataStoreWrapper(
        dataStorePreferences: DataStore<Preferences>
    ): PreferencesDataStoreWrapper {
        return PreferencesDataStoreWrapperImpl(dataStorePreferences = dataStorePreferences)
    }
}
