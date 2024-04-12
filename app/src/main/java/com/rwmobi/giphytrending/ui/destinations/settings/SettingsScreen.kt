/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.components.LoadingOverlay
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.theme.getDimension

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    apiMinEntries: Int,
    apiMaxEntries: Int,
    uiState: SettingsUIState,
    uiEvent: SettingsUIEvent,
    onShowSnackbar: suspend (String) -> Unit,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    Box(modifier = modifier.verticalScroll(state = rememberScrollState())) {
        uiState.apiMaxEntries?.let {
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    Settings(
                        modifier = Modifier.fillMaxSize(),
                        sliderValue = uiState.apiMaxEntries.toFloat(),
                        sliderRange = apiMinEntries.toFloat()..apiMaxEntries.toFloat(),
                        onSliderValueChange = { uiEvent.onUpdateApiMaxEntries(it.toInt()) },
                    )
                }

                WindowWidthSizeClass.Medium,
                WindowWidthSizeClass.Expanded,
                -> {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                        Settings(
                            modifier = Modifier.widthIn(max = 500.dp),
                            sliderValue = uiState.apiMaxEntries.toFloat(),
                            sliderRange = apiMinEntries.toFloat()..apiMaxEntries.toFloat(),
                            onSliderValueChange = { uiEvent.onUpdateApiMaxEntries(it.toInt()) },
                        )

                        Spacer(modifier = Modifier.weight(weight = 1f, fill = true))
                    }
                }
            }
        }

        AnimatedVisibility(visible = uiState.isLoading) {
            LoadingOverlay(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    sliderValue: Float,
    sliderRange: ClosedFloatingPointRange<Float>,
    onSliderValueChange: (Float) -> Unit,
) {
    val context = LocalContext.current
    val dimension = LocalConfiguration.current.getDimension()

    Column(
        modifier = modifier.padding(all = dimension.defaultFullPadding),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.api_max_description),
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimension.defaultHalfPadding),
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(id = R.string.apimax_desc),
        )

        Spacer(modifier = Modifier.height(height = dimension.defaultFullPadding))

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimension.defaultFullPadding),
            valueRange = sliderRange,
            value = sliderValue,
            onValueChange = onSliderValueChange,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimension.defaultFullPadding),
            textAlign = TextAlign.Center,
            text = "${sliderValue.toInt()}",
        )

        Spacer(modifier = Modifier.weight(1.0f))

        val annotatedText = getAnnotatedFootnote()
        ClickableText(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
            },
        )
    }
}

@Composable
private fun getAnnotatedFootnote(): AnnotatedString {
    val annotatedTextSpanStyle = SpanStyle(color = MaterialTheme.colorScheme.onSurface)
    val underlineSpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline,
    )

    return buildAnnotatedString {
        val str = stringResource(id = R.string.acknowledgement_sampleapp)
        val startIndex = str.indexOf("https://")
        val endIndex = str.length

        withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)) {
            withStyle(style = annotatedTextSpanStyle) {
                append(str.substring(0, startIndex))
            }

            pushStringAnnotation(tag = "URL", annotation = str.substring(startIndex, endIndex))
            withStyle(style = underlineSpanStyle) {
                append(str.substring(startIndex, endIndex))
            }

            pop()
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@PreviewDynamicColors
@Composable
private fun Preview() {
    GiphyTrendingTheme {
        Surface {
            SettingsScreen(
                modifier = Modifier.fillMaxSize(),
                windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
                apiMinEntries = 25,
                apiMaxEntries = 250,
                uiState = SettingsUIState(
                    apiMaxEntries = 80,
                    isLoading = false,
                ),
                uiEvent = SettingsUIEvent(
                    onErrorShown = {},
                    onUpdateApiMaxEntries = {},
                ),
                onShowSnackbar = {},
            )
        }
    }
}
