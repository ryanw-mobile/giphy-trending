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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.components.GiphyItem
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemsProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.theme.getDimension
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

    Box(modifier = modifier) {
        uiState.giphyImageItems?.let {
            if (it.isNotEmpty()) {
                TrendingList(
                    modifier = Modifier.fillMaxSize(),
                    giphyImageItems = it,
                    isLoading = uiState.isLoading,
                    onRefresh = uiEvent.onRefresh,
                    imageLoader = imageLoader,
                    onClickToShare = {},
                    onClickToOpen = {},
                )
            } else {
                // TODO: show empty screen
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrendingList(
    modifier: Modifier = Modifier,
    giphyImageItems: List<GiphyImageItem>,
    isLoading: Boolean,
    imageLoader: ImageLoader,
    onRefresh: () -> Unit,
    onClickToShare: (String) -> Unit,
    onClickToOpen: (String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()
    val contentDescriptionTrendingList = stringResource(R.string.content_description_trending_list)
    val pullRefreshState = rememberPullToRefreshState()

    Box(modifier.nestedScroll(connection = pullRefreshState.nestedScrollConnection)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = contentDescriptionTrendingList },
        ) {
            itemsIndexed(giphyImageItems) { index, giphyImageItem ->
                GiphyItem(
                    modifier = Modifier.fillMaxWidth(),
                    giphyImageItem = giphyImageItem,
                    imageLoader = imageLoader,
                    onClickToOpen = onClickToOpen,
                    onClickToShare = onClickToShare,
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
private fun TrendingListPreview(
    @PreviewParameter(GiphyImageItemsProvider::class) giphyImageItems: List<GiphyImageItem>,
) {
    GiphyTrendingTheme {
        Surface {
            TrendingList(
                modifier = Modifier.fillMaxSize(),
                giphyImageItems = giphyImageItems,
                isLoading = false,
                onRefresh = {},
                imageLoader = ImageLoader(LocalContext.current),
                onClickToShare = {},
                onClickToOpen = {},
            )
        }
    }
}
