/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.core.net.toUri
import coil3.ImageLoader
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.components.GiphyStaggeredGrid
import com.rwmobi.giphytrending.ui.components.NoDataScreen
import com.rwmobi.giphytrending.ui.components.SearchTextField
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.utils.downloadImage
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    useCardLayout: Boolean,
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
    val focusManager: FocusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { coroutineScope.launch { focusManager.clearFocus() } },
                )
            }
            .semantics {
                testTag = "layoutBox"
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            SearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = GiphyTrendingTheme.dimens.defaultHalfPadding),
                keyword = uiState.keyword,
                focusManager = focusManager,
                onClearKeyword = uiEvent.onClearKeyword,
                onUpdateKeyword = { uiEvent.onUpdateKeyword(it.take(uiState.keywordMaxLength)) },
                onSearch = uiEvent.onSearch,
            )

            uiState.gifObjects?.let { giphyImageItems ->
                if (giphyImageItems.isNotEmpty()) {
                    GiphyStaggeredGrid(
                        modifier = Modifier.fillMaxSize(),
                        gifObjects = giphyImageItems,
                        listContentDescription = stringResource(R.string.content_description_search_results),
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
                        text = stringResource(R.string.there_is_nothing_to_show_try_another_search),
                    )
                }
            }
        }

        DisposableEffect(true) {
            uiEvent.onFetchLastSuccessfulSearch()

            onDispose {
                coroutineScope.launch {
                    focusManager.clearFocus()
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
