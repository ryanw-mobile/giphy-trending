/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil3.ImageLoader
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.ui.components.GiphyStaggeredGrid
import com.rwmobi.giphytrending.ui.components.NoDataScreen
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemsProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.utils.downloadImage
import com.rwmobi.giphytrending.ui.utils.getPreviewWindowSizeClass
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingListScreen(
    modifier: Modifier = Modifier,
    useCardLayout: Boolean,
    imageLoader: ImageLoader,
    uiState: TrendingUIState,
    uiActions: TrendingUIActions,
    effectFlow: Flow<TrendingEffect>,
    onShowSnackbar: suspend (String) -> Unit,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            onShowSnackbar(errorMessageText)
            uiActions.onErrorShown(errorMessage.id)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            effectFlow.collect { effect ->
                when (effect) {
                    is TrendingEffect.ShowSnackbar -> onShowSnackbar(effect.message)
                }
            }
        }
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val clipboardSnackbarText = stringResource(id = R.string.clipboard_copied)
    val contentDescriptionPullToRefresh = stringResource(R.string.content_description_pull_to_refresh)

    PullToRefreshBox(
        modifier = modifier.semantics { contentDescription = contentDescriptionPullToRefresh },
        isRefreshing = uiState.isLoading,
        onRefresh = {
            uiActions.onRefresh()
        },
    ) {
        uiState.gifObjects?.let { giphyImageItems ->
            if (giphyImageItems.isNotEmpty()) {
                GiphyStaggeredGrid(
                    modifier = Modifier.fillMaxSize(),
                    gifObjects = giphyImageItems,
                    listContentDescription = stringResource(R.string.content_description_trending_list),
                    useCardLayout = useCardLayout,
                    imageLoader = imageLoader,
                    requestScrollToTop = uiState.requestScrollToTop,
                    onClickToDownload = { imageUrl ->
                        downloadImage(
                            imageUrl = imageUrl,
                            coroutineScope = coroutineScope,
                            context = context,
                            onSuccess = { uiActions.onQueueDownloadSuccess() },
                            onError = { uiActions.onQueueDownloadFailed() },
                        )
                    },
                    onClickToOpen = { url -> context.startBrowserActivity(url = url) },
                    onClickToShare = { url ->
                        val uri = url.toUri().buildUpon().scheme("https").build()
                        clipboardManager.setText(AnnotatedString(uri.toString()))
                        coroutineScope.launch {
                            onShowSnackbar(clipboardSnackbarText)
                        }
                    },
                    onScrolledToTop = uiActions.onScrolledToTop,
                )
            } else if (!uiState.isLoading) {
                NoDataScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), // to support pull to refresh
                    stringResource(R.string.there_is_nothing_to_show_try_pull_to_reload),
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun Preview(
    @PreviewParameter(GiphyImageItemsProvider::class) gifObjects: List<GifObject>,
) {
    GiphyTrendingTheme {
        Surface {
            TrendingListScreen(
                modifier = Modifier.fillMaxSize(),
                useCardLayout = getPreviewWindowSizeClass().widthSizeClass != WindowWidthSizeClass.Compact,
                imageLoader = ImageLoader(LocalContext.current),
                uiState = TrendingUIState(
                    gifObjects = gifObjects,
                    isLoading = false,
                ),
                uiActions = TrendingUIActions(
                    onRefresh = {},
                    onErrorShown = {},
                    onScrolledToTop = {},
                    onQueueDownloadFailed = {},
                    onQueueDownloadSuccess = {},
                ),
                effectFlow = emptyFlow(),
                onShowSnackbar = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun NoDataPreview() {
    GiphyTrendingTheme {
        Surface {
            TrendingListScreen(
                modifier = Modifier.fillMaxSize(),
                useCardLayout = getPreviewWindowSizeClass().widthSizeClass != WindowWidthSizeClass.Compact,
                imageLoader = ImageLoader(LocalContext.current),
                uiState = TrendingUIState(
                    gifObjects = emptyList(),
                    isLoading = false,
                ),
                uiActions = TrendingUIActions(
                    onRefresh = {},
                    onErrorShown = {},
                    onScrolledToTop = {},
                    onQueueDownloadFailed = {},
                    onQueueDownloadSuccess = {},
                ),
                effectFlow = emptyFlow(),
                onShowSnackbar = {},
            )
        }
    }
}
