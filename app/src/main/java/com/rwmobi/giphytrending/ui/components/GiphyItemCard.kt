/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun GiphyItemCard(
    modifier: Modifier = Modifier,
    gifObject: GifObject,
    imageLoader: ImageLoader,
    onClickToDownload: (imageUrl: String) -> Unit,
    onClickToOpen: (url: String) -> Unit,
    onClickToShare: (url: String) -> Unit,
) {
    Card(
        modifier = modifier.background(color = GiphyTrendingTheme.colorScheme.surface),
    ) {
        GiphyItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = GiphyTrendingTheme.dimens.defaultHalfPadding),
            gifObject = gifObject,
            showBottomDivider = false,
            imageLoader = imageLoader,
            onClickToDownload = { onClickToDownload(it) },
            onClickToOpen = { onClickToOpen(it) },
            onClickToShare = { onClickToShare(it) },
        )
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun Preview(
    @PreviewParameter(GiphyImageItemProvider::class) gifObject: GifObject,
) {
    GiphyTrendingTheme {
        Surface {
            GiphyItemCard(
                modifier = Modifier
                    .padding(all = 24.dp)
                    .fillMaxWidth(),
                gifObject = gifObject,
                imageLoader = ImageLoader(LocalContext.current),
                onClickToDownload = {},
                onClickToShare = {},
                onClickToOpen = {},
            )
        }
    }
}
