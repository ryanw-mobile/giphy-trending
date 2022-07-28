package uk.ryanwong.giphytrending.ui.settings

sealed class SettingsUIState {
    // Settings being so simple that it doesn't need a loading state right now
    object Ready : SettingsUIState()
    data class Error(val errMsg: String) : SettingsUIState()
}
