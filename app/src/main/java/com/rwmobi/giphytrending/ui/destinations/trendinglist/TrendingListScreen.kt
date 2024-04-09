/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.ImageLoader
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.components.GiphyItem
import com.rwmobi.giphytrending.ui.components.NoDataScreen
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemsProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.theme.getDimension
import com.rwmobi.giphytrending.ui.utils.downloadImageUsingMediaStore
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingListScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String) -> Unit,
    imageLoader: ImageLoader,
    uiState: TrendingUIState,
    uiEvent: TrendingUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val context = LocalContext.current
    var clipboardUrl by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()

    fun downloadImage(imageUrl: String) {
        if (!context.downloadImageUsingMediaStore(imageUrl = imageUrl)) {
            coroutineScope.launch {
                onShowSnackbar(context.getString(R.string.failed_to_download_file))
            }
        }
    }

    Box(modifier = modifier.nestedScroll(connection = pullRefreshState.nestedScrollConnection)) {
        uiState.giphyImageItems?.let { giphyImageItems ->
            if (giphyImageItems.isNotEmpty()) {
                TrendingList(
                    modifier = Modifier.fillMaxSize(),
                    giphyImageItems = giphyImageItems,
                    imageLoader = imageLoader,
                    onClickToDownload = { imageUrl -> downloadImage(imageUrl = imageUrl) },
                    onClickToOpen = { url -> context.startBrowserActivity(url = url) },
                    onClickToShare = { url -> clipboardUrl = url },
                )
            } else if (!uiState.isLoading) {
                NoDataScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), // to support pull to refresh
                )
            }
        }

        if (pullRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                if (!uiState.isLoading) {
                    delay(1000) // Trick to let user know work in progress
                    uiEvent.onRefresh()
                }
            }
        }

        LaunchedEffect(uiState.isLoading) {
            if (!uiState.isLoading) {
                pullRefreshState.endRefresh()
            } else {
                pullRefreshState.startRefresh()
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullRefreshState,
        )
    }

    val clipboardManager = LocalClipboardManager.current
    val snackbarText = stringResource(id = R.string.clipboard_copied)
    LaunchedEffect(clipboardUrl) {
        if (clipboardUrl.isNotEmpty()) {
            val uri = clipboardUrl.toUri().buildUpon().scheme("https").build()
            clipboardManager.setText(AnnotatedString(uri.toString()))
            onShowSnackbar(snackbarText)
            clipboardUrl = ""
        }
    }
}

@Composable
private fun TrendingList(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    giphyImageItems: List<GiphyImageItem>,
    onClickToDownload: (imageUrl: String) -> Unit,
    onClickToShare: (Url: String) -> Unit,
    onClickToOpen: (Url: String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()
    val contentDescriptionTrendingList = stringResource(R.string.content_description_trending_list)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = contentDescriptionTrendingList },
    ) {
        itemsIndexed(giphyImageItems) { index, giphyImageItem ->
            GiphyItem(
                modifier = Modifier.fillMaxWidth(),
                giphyImageItem = giphyImageItem,
                imageLoader = imageLoader,
                onClickToDownload = { onClickToDownload(it) },
                onClickToOpen = { onClickToOpen(it) },
                onClickToShare = { onClickToShare(it) },
            )

            if (index < giphyImageItems.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimension.grid_0_5),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.inverseSurface,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TrendingListScreenPreview(
    @PreviewParameter(GiphyImageItemsProvider::class) giphyImageItems: List<GiphyImageItem>,
) {
    GiphyTrendingTheme {
        Surface {
            TrendingListScreen(
                modifier = Modifier.fillMaxSize(),
                onShowSnackbar = {},
                imageLoader = ImageLoader(LocalContext.current),
                uiState = TrendingUIState(
                    giphyImageItems = giphyImageItems,
                    isLoading = false,
                ),
                uiEvent = TrendingUIEvent(
                    onRefresh = {},
                    onErrorShown = {},
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun TrendingListScreenNoDataPreview() {
    GiphyTrendingTheme {
        Surface {
            TrendingListScreen(
                modifier = Modifier.fillMaxSize(),
                onShowSnackbar = {},
                imageLoader = ImageLoader(LocalContext.current),
                uiState = TrendingUIState(
                    giphyImageItems = emptyList(),
                    isLoading = false,
                ),
                uiEvent = TrendingUIEvent(
                    onRefresh = {},
                    onErrorShown = {},
                ),
            )
        }
    }
}
