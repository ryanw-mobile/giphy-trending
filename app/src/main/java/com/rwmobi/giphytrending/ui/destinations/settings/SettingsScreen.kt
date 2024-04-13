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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.ui.components.LoadingOverlay
import com.rwmobi.giphytrending.ui.theme.Dimension
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
        if (uiState.apiRequestLimit != null && uiState.rating != null) {
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    Settings(
                        modifier = Modifier.fillMaxSize(),
                        rating = uiState.rating,
                        sliderValue = uiState.apiRequestLimit.toFloat(),
                        sliderRange = apiMinEntries.toFloat()..apiMaxEntries.toFloat(),
                        onUpdateApiRequestLimit = { uiEvent.onUpdateApiMaxEntries(it.toInt()) },
                        onUpdateRating = { uiEvent.onUpdateRating(it) },
                    )
                }

                WindowWidthSizeClass.Medium,
                WindowWidthSizeClass.Expanded,
                -> {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                        Settings(
                            modifier = Modifier
                                .widthIn(max = 500.dp)
                                .fillMaxHeight(),
                            rating = uiState.rating,
                            sliderValue = uiState.apiRequestLimit.toFloat(),
                            sliderRange = apiMinEntries.toFloat()..apiMaxEntries.toFloat(),
                            onUpdateApiRequestLimit = { uiEvent.onUpdateApiMaxEntries(it.toInt()) },
                            onUpdateRating = { uiEvent.onUpdateRating(it) },
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
    rating: Rating,
    sliderValue: Float,
    sliderRange: ClosedFloatingPointRange<Float>,
    onUpdateApiRequestLimit: (Float) -> Unit,
    onUpdateRating: (Rating) -> Unit,
) {
    val context = LocalContext.current
    val dimension = LocalConfiguration.current.getDimension()

    Column(
        modifier = modifier.padding(all = dimension.defaultFullPadding),
    ) {
        ItemsToLoad(
            dimension = dimension,
            sliderRange = sliderRange,
            sliderValue = sliderValue,
            onUpdateApiRequestLimit = onUpdateApiRequestLimit,
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = dimension.defaultFullPadding),
            thickness = 1.dp,
        )

        ImageRating(
            dimension = dimension,
            rating = rating,
            onUpdateRating = onUpdateRating,
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = dimension.grid_4, bottom = dimension.defaultFullPadding),
            thickness = 1.dp,
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(alignment = Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.version, BuildConfig.VERSION_NAME),
        )

        Spacer(modifier = Modifier.height(height = dimension.defaultHalfPadding))

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
private fun ItemsToLoad(
    dimension: Dimension,
    sliderRange: ClosedFloatingPointRange<Float>,
    sliderValue: Float,
    onUpdateApiRequestLimit: (Float) -> Unit,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        text = stringResource(id = R.string.api_max_description),
    )

    Spacer(modifier = Modifier.height(height = dimension.defaultHalfPadding))

    Slider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimension.defaultFullPadding),
        valueRange = sliderRange,
        value = sliderValue,
        onValueChange = onUpdateApiRequestLimit,
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimension.defaultFullPadding),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
        text = "${sliderValue.toInt()}",
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimension.defaultHalfPadding),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify,
        text = stringResource(id = R.string.apimax_desc),
    )
}

@Composable
private fun ImageRating(
    dimension: Dimension,
    rating: Rating,
    onUpdateRating: (Rating) -> Unit,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        text = stringResource(R.string.rating),
    )

    Spacer(modifier = Modifier.height(height = dimension.defaultHalfPadding))

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val currentDensity = LocalDensity.current
        CompositionLocalProvider(
            LocalDensity provides Density(currentDensity.density, fontScale = 1f),
        ) {
            for (ratingOption in Rating.entries) {
                TextButton(
                    modifier = Modifier
                        .weight(weight = 1f, fill = true)
                        .align(alignment = Alignment.CenterVertically),
                    shape = RectangleShape,
                    colors = if (ratingOption == rating) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors(),
                    onClick = { onUpdateRating(ratingOption) },
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        style = MaterialTheme.typography.labelLarge,
                        text = ratingOption.toString(),
                    )
                }

                if (ratingOption.ordinal < Rating.entries.lastIndex) {
                    Spacer(modifier = Modifier.width(width = dimension.grid_0_25))
                }
            }
        }
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimension.defaultHalfPadding),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify,
        text = stringResource(getRatingDescriptionRes(rating = rating)),
    )
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

private fun getRatingDescriptionRes(rating: Rating): Int {
    return when (rating) {
        Rating.G -> R.string.rating_g
        Rating.PG -> R.string.rating_pg
        Rating.PG_13 -> R.string.rating_pg_13
        Rating.R -> R.string.rating_r
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
                    apiRequestLimit = 80,
                    rating = Rating.G,
                    isLoading = false,
                ),
                uiEvent = SettingsUIEvent(
                    onErrorShown = {},
                    onUpdateApiMaxEntries = {},
                    onUpdateRating = {},
                ),
                onShowSnackbar = {},
            )
        }
    }
}
