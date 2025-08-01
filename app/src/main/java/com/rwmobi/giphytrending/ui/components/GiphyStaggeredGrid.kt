/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemsProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun GiphyStaggeredGrid(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    gifObjects: List<GifObject>,
    listContentDescription: String,
    requestScrollToTop: Boolean,
    useCardLayout: Boolean,
    onClickToDownload: (imageUrl: String) -> Unit,
    onClickToShare: (url: String) -> Unit,
    onClickToOpen: (url: String) -> Unit,
    onScrolledToTop: () -> Unit,
) {
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    LaunchedEffect(requestScrollToTop) {
        if (requestScrollToTop) {
            lazyStaggeredGridState.scrollToItem(index = 0)
            onScrolledToTop()
        }
    }

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = listContentDescription },
        state = lazyStaggeredGridState,
        columns = StaggeredGridCells.Adaptive(minSize = 320.dp),
        contentPadding = PaddingValues(all = GiphyTrendingTheme.dimens.defaultHalfPadding),
    ) {
        itemsIndexed(
            items = gifObjects,
            key = { _, giphyImageItem -> giphyImageItem.id },
        ) { index, giphyImageItem ->
            if (useCardLayout) {
                GiphyItemCard(
                    modifier = Modifier.padding(all = GiphyTrendingTheme.dimens.defaultHalfPadding),
                    gifObject = giphyImageItem,
                    imageLoader = imageLoader,
                    onClickToDownload = onClickToDownload,
                    onClickToOpen = onClickToOpen,
                    onClickToShare = onClickToShare,
                )
            } else {
                GiphyItem(
                    modifier = Modifier.fillMaxWidth(),
                    gifObject = giphyImageItem,
                    showBottomDivider = (index < gifObjects.lastIndex),
                    imageLoader = imageLoader,
                    onClickToDownload = { onClickToDownload(it) },
                    onClickToOpen = { onClickToOpen(it) },
                    onClickToShare = { onClickToShare(it) },
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun Preview(
    @PreviewParameter(GiphyImageItemsProvider::class) gifObjects: List<GifObject>,
) {
    GiphyTrendingTheme {
        Surface {
            GiphyStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                imageLoader = ImageLoader(LocalContext.current),
                gifObjects = gifObjects,
                listContentDescription = "",
                requestScrollToTop = false,
                useCardLayout = false,
                onClickToDownload = {},
                onClickToShare = {},
                onClickToOpen = {},
                onScrolledToTop = {},
            )
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun PreviewCardLayout(
    @PreviewParameter(GiphyImageItemsProvider::class) gifObjects: List<GifObject>,
) {
    GiphyTrendingTheme {
        Surface {
            GiphyStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                imageLoader = ImageLoader(LocalContext.current),
                gifObjects = gifObjects,
                listContentDescription = "",
                requestScrollToTop = false,
                useCardLayout = true,
                onClickToDownload = {},
                onClickToShare = {},
                onClickToOpen = {},
                onScrolledToTop = {},
            )
        }
    }
}
