package uk.ryanwong.giphytrending.ui.trending

sealed class TrendingUIState {
    object Ready : TrendingUIState()
    object Loading : TrendingUIState()
    data class Error(val errMsg: String) : TrendingUIState()
}
