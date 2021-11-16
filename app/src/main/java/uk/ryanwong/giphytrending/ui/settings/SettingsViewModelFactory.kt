package uk.ryanwong.giphytrending.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory @Inject constructor(private val repository: UserPreferencesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}