package uk.ryanwong.giphytrending.ui.trending

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TrendingViewModelTest{

    private lateinit var viewModel : TrendingViewModel

    @Before
    fun setup() {
        viewModel = TrendingViewModelFactory(FakeGiphyRepository, userPreferencesRepository)
    }



}