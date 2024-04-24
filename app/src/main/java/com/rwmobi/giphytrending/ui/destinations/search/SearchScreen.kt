/*
 * Copyright (c) 2024. Ryan Wong
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.core.net.toUri
import coil.ImageLoader
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.components.GiphyStaggeredGrid
import com.rwmobi.giphytrending.ui.components.NoDataScreen
import com.rwmobi.giphytrending.ui.components.SearchTextField
import com.rwmobi.giphytrending.ui.theme.getDimension
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
    val dimension = LocalConfiguration.current.getDimension()
    val coroutineScope = rememberCoroutineScope()
    val clipboardHistory = remember { mutableStateListOf<String>() }
    val focusManager: FocusManager = LocalFocusManager.current
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    Box(
        modifier = modifier
            .nestedScroll(connection = nestedScrollConnection)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { coroutineScope.launch { focusManager.clearFocus() } },
                )
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            SearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimension.defaultHalfPadding),
                keyword = uiState.keyword,
                focusManager = focusManager,
                onClearKeyword = uiEvent.onClearKeyword,
                onUpdateKeyword = { uiEvent.onUpdateKeyword(it.take(uiState.keywordMaxLength)) },
                onSearch = uiEvent.onSearch,
            )

            uiState.giphyImageItems?.let { giphyImageItems ->
                if (giphyImageItems.isNotEmpty()) {
                    GiphyStaggeredGrid(
                        modifier = Modifier.fillMaxSize(),
                        giphyImageItems = giphyImageItems,
                        useCardLayout = useCardLayout,
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
                } else if (!uiState.isLoading) {
                    NoDataScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()), // to support pull to refresh
                    )
                }
            }
        }

        DisposableEffect(true) {
            onDispose {
                uiEvent.onClearKeyword
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
