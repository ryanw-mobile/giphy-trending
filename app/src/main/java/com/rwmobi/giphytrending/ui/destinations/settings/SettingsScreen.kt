/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.settings

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.utils.getPreviewWindowSizeClass
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    isLargeScreen: Boolean,
    apiMinEntries: Int,
    apiMaxEntries: Int,
    uiState: SettingsUIState,
    uiEvent: SettingsUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val context = LocalContext.current
    Box(modifier = modifier) {
        if (uiState.apiRequestLimit != null && uiState.rating != null) {
            if (!isLargeScreen) {
                val scrollState = rememberScrollState()

                Settings(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState),
                    rating = uiState.rating,
                    sliderValue = uiState.apiRequestLimit.toFloat(),
                    sliderRange = apiMinEntries.toFloat()..apiMaxEntries.toFloat(),
                    onUpdateApiRequestLimit = { uiEvent.onUpdateApiMaxEntries(it.toInt()) },
                    onUpdateRating = { uiEvent.onUpdateRating(it) },
                    onBrowseUrl = { url -> context.startBrowserActivity(url = url) },
                )

                LaunchedEffect(uiState.requestScrollToTop) {
                    if (uiState.requestScrollToTop) {
                        scrollState.scrollTo(value = 0)
                        uiEvent.onScrolledToTop()
                    }
                }
            } else {
                val scrollState = rememberScrollState()

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState),
                ) {
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
                        onBrowseUrl = { url -> context.startBrowserActivity(url = url) },
                    )

                    Spacer(modifier = Modifier.weight(weight = 1f, fill = true))
                }

                LaunchedEffect(uiState.requestScrollToTop) {
                    if (uiState.requestScrollToTop) {
                        scrollState.scrollTo(value = 0)
                        uiEvent.onScrolledToTop()
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
    onBrowseUrl: (url: String) -> Unit,
) {
    Column(
        modifier = modifier.padding(all = GiphyTrendingTheme.dimens.defaultFullPadding),
    ) {
        ItemsToLoad(
            modifier = Modifier.fillMaxWidth(),
            sliderRange = sliderRange,
            sliderValue = sliderValue,
            onUpdateApiRequestLimit = onUpdateApiRequestLimit,
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = GiphyTrendingTheme.dimens.defaultFullPadding),
            thickness = 1.dp,
        )

        ImageRating(
            modifier = Modifier.fillMaxWidth(),
            rating = rating,
            onUpdateRating = onUpdateRating,
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = GiphyTrendingTheme.dimens.grid_4, bottom = GiphyTrendingTheme.dimens.defaultFullPadding),
            thickness = 1.dp,
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(alignment = Alignment.CenterHorizontally),
            style = GiphyTrendingTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.version, BuildConfig.VERSION_NAME),
        )

        Spacer(modifier = Modifier.height(height = GiphyTrendingTheme.dimens.defaultHalfPadding))

        val annotatedText = getAnnotatedFootnote()
        ClickableText(
            modifier = Modifier.fillMaxWidth(),
            style = GiphyTrendingTheme.typography.bodyMedium,
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        onBrowseUrl(Uri.parse(annotation.item).toString())
                    }
            },
        )
    }
}

@Composable
private fun ItemsToLoad(
    modifier: Modifier = Modifier,
    sliderRange: ClosedFloatingPointRange<Float>,
    sliderValue: Float,
    onUpdateApiRequestLimit: (Float) -> Unit,
) {
    var tempSliderValue by remember { mutableFloatStateOf(sliderValue) }
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = GiphyTrendingTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.api_max_description),
        )

        Spacer(modifier = Modifier.height(height = GiphyTrendingTheme.dimens.defaultHalfPadding))

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GiphyTrendingTheme.dimens.defaultFullPadding)
                .semantics { contentDescription = context.getString(R.string.content_description_slider) },
            valueRange = sliderRange,
            value = tempSliderValue,
            onValueChange = { tempSliderValue = it },
            onValueChangeFinished = {
                // Update the actual value when the user finishes dragging the slider
                onUpdateApiRequestLimit(tempSliderValue)
            },
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = GiphyTrendingTheme.dimens.defaultFullPadding)
                .align(Alignment.CenterHorizontally),
            style = GiphyTrendingTheme.typography.bodyMedium,
            text = "${tempSliderValue.toInt()}",
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = GiphyTrendingTheme.dimens.defaultHalfPadding),
            style = GiphyTrendingTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            text = stringResource(id = R.string.apimax_desc),
        )
    }
}

@Composable
private fun ImageRating(
    modifier: Modifier = Modifier,
    rating: Rating,
    onUpdateRating: (Rating) -> Unit,
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = GiphyTrendingTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.rating),
        )

        Spacer(modifier = Modifier.height(height = GiphyTrendingTheme.dimens.defaultHalfPadding))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = GiphyTrendingTheme.shapes.medium)
                .semantics {
                    contentDescription = context.getString(R.string.content_description_rating_selector)
                },
        ) {
            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(currentDensity.density, fontScale = 1f),
            ) {
                for (ratingOption in Rating.entries) {
                    TextButton(
                        modifier = Modifier
                            .height(intrinsicSize = IntrinsicSize.Max)
                            .weight(weight = 1f, fill = true)
                            .align(alignment = Alignment.CenterVertically)
                            .semantics {
                                role = Role.RadioButton
                                if (ratingOption == rating) {
                                    stateDescription = context.getString(R.string.selected)
                                } else {
                                    stateDescription = context.getString(R.string.not_selected)
                                }
                            },
                        shape = RectangleShape,
                        colors = if (ratingOption == rating) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors(),
                        onClick = { onUpdateRating(ratingOption) },
                    ) {
                        Text(
                            modifier = Modifier.wrapContentSize(),
                            style = GiphyTrendingTheme.typography.labelLarge,
                            text = ratingOption.toString(),
                        )
                    }

                    if (ratingOption.ordinal < Rating.entries.lastIndex) {
                        Spacer(modifier = Modifier.width(width = GiphyTrendingTheme.dimens.grid_0_25))
                    }
                }
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = GiphyTrendingTheme.dimens.defaultHalfPadding),
            style = GiphyTrendingTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            text = stringResource(getRatingDescriptionRes(rating = rating)),
        )
    }
}

@Composable
private fun getAnnotatedFootnote(): AnnotatedString {
    val annotatedTextSpanStyle = SpanStyle(color = GiphyTrendingTheme.colorScheme.onSurface)
    val underlineSpanStyle = SpanStyle(
        color = GiphyTrendingTheme.colorScheme.primary,
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

private fun getRatingDescriptionRes(rating: Rating): Int = when (rating) {
    Rating.G -> R.string.rating_g
    Rating.PG -> R.string.rating_pg
    Rating.PG_13 -> R.string.rating_pg_13
    Rating.R -> R.string.rating_r
}

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
                isLargeScreen = getPreviewWindowSizeClass().widthSizeClass != WindowWidthSizeClass.Compact,
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
                    onScrolledToTop = {},
                    onShowSnackbar = {},
                ),
            )
        }
    }
}
