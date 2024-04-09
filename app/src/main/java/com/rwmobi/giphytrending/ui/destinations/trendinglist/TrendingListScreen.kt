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
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemsProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.theme.getDimension
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity
import kotlinx.coroutines.delay

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
    val dimension = LocalConfiguration.current.getDimension()

    Box(modifier = modifier) {
        uiState.giphyImageItems?.let { giphyImageItems ->
            if (giphyImageItems.isNotEmpty()) {
                TrendingList(
                    modifier = Modifier.fillMaxSize(),
                    giphyImageItems = giphyImageItems,
                    isLoading = uiState.isLoading,
                    onRefresh = uiEvent.onRefresh,
                ) { index, giphyImageItem ->
                    GiphyItem(
                        modifier = Modifier.fillMaxWidth(),
                        giphyImageItem = giphyImageItem,
                        imageLoader = imageLoader,
                        onClickToDownload = { },
                        onClickToOpen = { url -> context.startBrowserActivity(src = url) },
                        onClickToShare = { clipboardUrl = it },
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
            } else if (!uiState.isLoading) {
                // TODO: show empty screen
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrendingList(
    modifier: Modifier = Modifier,
    giphyImageItems: List<GiphyImageItem>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    listItemLayout: @Composable (index: Int, giphyImageItem: GiphyImageItem) -> Unit,
) {
    val contentDescriptionTrendingList = stringResource(R.string.content_description_trending_list)
    val pullRefreshState = rememberPullToRefreshState()

    Box(modifier.nestedScroll(connection = pullRefreshState.nestedScrollConnection)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = contentDescriptionTrendingList },
        ) {
            itemsIndexed(giphyImageItems) { index, giphyImageItem ->
                listItemLayout
            }
        }

        if (pullRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                if (!isLoading) {
                    delay(1000) // Trick to let user know work in progress
                    onRefresh()
                }
            }
        }

        LaunchedEffect(isLoading) {
            if (!isLoading) {
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
