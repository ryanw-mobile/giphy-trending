/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.destinations.trendinglist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewFontScale
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

@Composable
internal fun TrendingListCompact(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    giphyImageItems: List<GiphyImageItem>,
    requestScrollToTop: Boolean,
    onClickToDownload: (imageUrl: String) -> Unit,
    onClickToShare: (url: String) -> Unit,
    onClickToOpen: (url: String) -> Unit,
    onScrolledToTop: () -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()
    val contentDescriptionTrendingList = stringResource(R.string.content_description_trending_list)
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = contentDescriptionTrendingList },
        state = lazyListState,
    ) {
        itemsIndexed(
            items = giphyImageItems,
            key = { _, giphyImageItem -> giphyImageItem.id },
        ) { index, giphyImageItem ->
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
                )
            }
        }
    }

    LaunchedEffect(requestScrollToTop) {
        if (requestScrollToTop) {
            lazyListState.scrollToItem(index = 0)
            onScrolledToTop()
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun Preview(
    @PreviewParameter(GiphyImageItemsProvider::class) giphyImageItems: List<GiphyImageItem>,
) {
    GiphyTrendingTheme {
        Surface {
            TrendingListCompact(
                modifier = Modifier.fillMaxSize(),
                imageLoader = ImageLoader(LocalContext.current),
                giphyImageItems = giphyImageItems,
                requestScrollToTop = false,
                onClickToDownload = {},
                onClickToShare = {},
                onClickToOpen = {},
                onScrolledToTop = {},
            )
        }
    }
}
