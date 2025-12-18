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
import androidx.compose.runtime.mutableStateListOf
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingListScreen(
    modifier: Modifier = Modifier,
    useCardLayout: Boolean,
    imageLoader: ImageLoader,
    uiState: TrendingUIState,
    uiEvent: TrendingUIEvent,
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
    val coroutineScope = rememberCoroutineScope()
    val clipboardHistory = remember { mutableStateListOf<String>() }
    val contentDescriptionPullToRefresh = stringResource(R.string.content_description_pull_to_refresh)

    PullToRefreshBox(
        modifier = modifier.semantics { contentDescription = contentDescriptionPullToRefresh },
        isRefreshing = uiState.isLoading,
        onRefresh = {
            uiEvent.onRefresh()
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
                            onSuccess = uiEvent.onQueueDownloadSuccess,
                            onError = uiEvent.onQueueDownloadFailed,
                        )
                    },
                    onClickToOpen = { url -> context.startBrowserActivity(url = url) },
                    onClickToShare = { url -> clipboardHistory.add(url) },
                    onScrolledToTop = uiEvent.onScrolledToTop,
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

    val clipboardManager = LocalClipboardManager.current
    val snackbarText = stringResource(id = R.string.clipboard_copied)
    LaunchedEffect(clipboardHistory.lastOrNull()) {
        clipboardHistory.lastOrNull()?.let { url ->
            val uri = url.toUri().buildUpon().scheme("https").build()
            clipboardManager.setText(AnnotatedString(uri.toString()))
            uiEvent.onShowSnackbar(snackbarText)
            clipboardHistory.remove(url) // Remove after processing to avoid re-triggering
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
                uiEvent = TrendingUIEvent(
                    onRefresh = {},
                    onErrorShown = {},
                    onShowSnackbar = {},
                    onScrolledToTop = {},
                    onQueueDownloadFailed = {},
                    onQueueDownloadSuccess = {},
                ),
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
                uiEvent = TrendingUIEvent(
                    onRefresh = {},
                    onErrorShown = {},
                    onShowSnackbar = {},
                    onScrolledToTop = {},
                    onQueueDownloadFailed = {},
                    onQueueDownloadSuccess = {},
                ),
            )
        }
    }
}
