/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
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
import com.rwmobi.giphytrending.ui.components.SearchTextField
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemsProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.utils.downloadImage
import com.rwmobi.giphytrending.ui.utils.getPreviewWindowSizeClass
import com.rwmobi.giphytrending.ui.utils.startBrowserActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    useCardLayout: Boolean,
    imageLoader: ImageLoader,
    uiState: SearchUIState,
    uiActions: SearchUIActions,
    effectFlow: Flow<SearchEffect>,
    onShowSnackbar: suspend (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        uiActions.onFetchLastSuccessfulSearch()
    }

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
                    is SearchEffect.ShowSnackbar -> onShowSnackbar(effect.message)
                }
            }
        }
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val clipboardSnackbarText = stringResource(id = R.string.clipboard_copied)
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        SearchTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = GiphyTrendingTheme.dimens.defaultFullPadding),
            keyword = uiState.keyword,
            focusManager = focusManager,
            onUpdateKeyword = uiActions.onUpdateKeyword,
            onClearKeyword = uiActions.onClearKeyword,
            onSearch = uiActions.onSearch,
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
                        .verticalScroll(rememberScrollState()),
                    stringResource(R.string.there_is_nothing_to_show_try_another_search),
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
            SearchScreen(
                modifier = Modifier.fillMaxSize(),
                useCardLayout = getPreviewWindowSizeClass().widthSizeClass != androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Compact,
                imageLoader = ImageLoader(LocalContext.current),
                uiState = SearchUIState(
                    gifObjects = gifObjects,
                    isLoading = false,
                ),
                uiActions = SearchUIActions(
                    onFetchLastSuccessfulSearch = {},
                    onUpdateKeyword = {},
                    onClearKeyword = {},
                    onSearch = {},
                    onScrolledToTop = {},
                    onQueueDownloadFailed = {},
                    onQueueDownloadSuccess = {},
                    onErrorShown = {},
                ),
                effectFlow = emptyFlow(),
                onShowSnackbar = {},
            )
        }
    }
}
