package uk.ryanwong.giphytrending.ui.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class TrendingViewModelFactory @Inject constructor(
    private val giphyRepository: GiphyRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrendingViewModel::class.java)) {
            return TrendingViewModel(giphyRepository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}