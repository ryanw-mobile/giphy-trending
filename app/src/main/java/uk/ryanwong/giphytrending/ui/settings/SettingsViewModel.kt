package uk.ryanwong.giphytrending.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import uk.ryanwong.giphytrending.GiphyApplication
import uk.ryanwong.giphytrending.data.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlin.math.max

// Minimum entries to request from server
private const val API_MIN = 50

class SettingsViewModel @Inject constructor(private val repository: UserPreferencesRepository) :
    ViewModel() {

    // The seekbar progress has to deduct the minimum value
    val apiMaxEntriesProgress = repository.apiMaxEntries.map { apiMax ->
        max(apiMax.minus(API_MIN), 0)
    }

    init {
        GiphyApplication.appComponent.inject(this)
    }

    fun translateMaxApiEntries(value: Int) = value.plus(API_MIN).toString()

    fun setApiMax(maxApiEntries: Int) {
        repository.updateApiMax(maxApiEntries.plus(API_MIN))
    }
}