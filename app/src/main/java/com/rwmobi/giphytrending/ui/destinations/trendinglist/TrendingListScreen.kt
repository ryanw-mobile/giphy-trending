/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.components.GiphyItem
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.theme.getDimension
import timber.log.Timber

@Composable
fun TrendingListScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String) -> Unit,
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
                    onCopyImageUrl = {},
                    onOpenInBrowser = {},
                )
            } else {
                // show empty screen
            }
        }

//        AnimatedVisibility(visible = uiState.isLoading) {
//            LoadingOverlay(
//                modifier = Modifier.fillMaxSize(),
//            )
//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrendingList(
    modifier: Modifier = Modifier,
    giphyImageItems: List<GiphyImageItem>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onCopyImageUrl: (String) -> Unit,
    onOpenInBrowser: (String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()
    val contentDescriptionTrendingList = stringResource(R.string.content_description_trending_list)

    val pullRefreshState = rememberPullToRefreshState()
    Box(Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = dimension.grid_2),
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = contentDescriptionTrendingList },
        ) {
            itemsIndexed(giphyImageItems) { index, giphyImageItem ->
                GiphyItem(giphyImageItem = giphyImageItem, onClickToOpen = {}, onClickToShare = {})
            }
        }

        if (pullRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                if (!isLoading) {
                    Timber.d("!!!")
                    onRefresh()
                }
            }
        }

        LaunchedEffect(isLoading) {
            if (!isLoading) {
                Timber.d("update state to stopRefresh")
                pullRefreshState.endRefresh()
            } else {
                Timber.d("update state to startRefresh")
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
private fun TrendingListPreview() {
    GiphyTrendingTheme {
        Surface {
            TrendingList(
                modifier = Modifier.fillMaxSize(),
                giphyImageItems = emptyList(),
                isLoading = false,
                onRefresh = {},
                onCopyImageUrl = {},
                onOpenInBrowser = {},
            )
        }
    }
}
