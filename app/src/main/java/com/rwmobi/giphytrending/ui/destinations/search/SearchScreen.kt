/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.core.net.toUri
import coil.ImageLoader
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.components.NoDataScreen
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingListCompact
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingStaggeredGrid
import com.rwmobi.giphytrending.ui.theme.getDimension
import com.rwmobi.giphytrending.ui.utils.downloadImage
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    imageLoader: ImageLoader,
    uiState: SearchUIState,
    uiEvent: SearchUIEvent,
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
    val dimension = LocalConfiguration.current.getDimension()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    Box(modifier = modifier.nestedScroll(connection = nestedScrollConnection)) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            var tempKeyword by remember { mutableStateOf(uiState.keyword) }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimension.defaultFullPadding),
                value = tempKeyword,
                onValueChange = { tempKeyword = it },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = { uiEvent.onSearch(tempKeyword) }),
            )

            uiState.giphyImageItems?.let { giphyImageItems ->
                if (giphyImageItems.isNotEmpty()) {
                    when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.Compact -> {
                            TrendingListCompact(
                                modifier = Modifier.fillMaxSize(),
                                giphyImageItems = giphyImageItems,
                                imageLoader = imageLoader,
                                requestScrollToTop = uiState.requestScrollToTop,
                                onClickToDownload = { imageUrl ->
                                    downloadImage(
                                        imageUrl = imageUrl,
                                        coroutineScope = coroutineScope,
                                        context = context,
                                        onError = { uiEvent.onShowSnackbar(context.getString(R.string.failed_to_download_file)) },
                                    )
                                },
                                onClickToOpen = { url -> context.startBrowserActivity(url = url) },
                                onClickToShare = { url -> clipboardHistory.add(url) },
                                onScrolledToTop = uiEvent.onScrolledToTop,
                            )
                        }

                        WindowWidthSizeClass.Medium,
                        WindowWidthSizeClass.Expanded,
                        -> {
                            TrendingStaggeredGrid(
                                modifier = Modifier.fillMaxSize(),
                                giphyImageItems = giphyImageItems,
                                imageLoader = imageLoader,
                                requestScrollToTop = uiState.requestScrollToTop,
                                onClickToDownload = { imageUrl ->
                                    downloadImage(
                                        imageUrl = imageUrl,
                                        coroutineScope = coroutineScope,
                                        context = context,
                                        onError = { uiEvent.onShowSnackbar(context.getString(R.string.failed_to_download_file)) },
                                    )
                                },
                                onClickToOpen = { url -> context.startBrowserActivity(url = url) },
                                onClickToShare = { url -> clipboardHistory.add(url) },
                                onScrolledToTop = uiEvent.onScrolledToTop,
                            )
                        }
                    }
                } else if (!uiState.isLoading) {
                    NoDataScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()), // to support pull to refresh
                    )
                }
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
