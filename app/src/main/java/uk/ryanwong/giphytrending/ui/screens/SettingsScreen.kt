package uk.ryanwong.giphytrending.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.ryanwong.giphytrending.R
import uk.ryanwong.giphytrending.ui.settings.SettingsViewModel
import uk.ryanwong.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
) {
    val uiState = settingsViewModel.settingsUIState.collectAsStateWithLifecycle()
}

@Composable
private fun Settings(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.api_max_description),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.apimax_desc),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = 0f,
            onValueChange = {},
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "0",
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.acknowledgement_sampleapp),
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingsPreview() {
    GiphyTrendingTheme {
        Settings(
            modifier = Modifier.fillMaxSize(),
        )
    }
}
