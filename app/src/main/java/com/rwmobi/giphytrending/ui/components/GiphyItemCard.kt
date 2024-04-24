/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.theme.Dimension

@Composable
fun GiphyItemCard(
    modifier: Modifier = Modifier,
    dimension: Dimension,
    giphyImageItem: GiphyImageItem,
    imageLoader: ImageLoader,
    onClickToDownload: (imageUrl: String) -> Unit,
    onClickToOpen: (url: String) -> Unit,
    onClickToShare: (url: String) -> Unit,
) {
    Card(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
    ) {
        GiphyItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.defaultHalfPadding),
            giphyImageItem = giphyImageItem,
            showBottomDivider = false,
            imageLoader = imageLoader,
            onClickToDownload = { onClickToDownload(it) },
            onClickToOpen = { onClickToOpen(it) },
            onClickToShare = { onClickToShare(it) },
        )
    }
}
