/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.previewparameter.GiphyImageItemProvider
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.theme.getDimension

@Composable
fun GiphyItem(
    modifier: Modifier = Modifier,
    giphyImageItem: GiphyImageItem,
    imageLoader: ImageLoader,
    onClickToOpen: (String) -> Unit,
    onClickToShare: (String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = dimension.minListItemHeight)
                .padding(
                    horizontal = dimension.defaultFullPadding,
                    vertical = dimension.defaultHalfPadding,
                ),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = giphyImageItem.title,
        )

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.3f)),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(giphyImageItem.previewUrl)
                .crossfade(true)
                .build(),
            fallback = painterResource(R.drawable.twotone_insert_photo_24),
            error = painterResource(R.drawable.twotone_insert_photo_24),
            placeholder = painterResource(R.drawable.twotone_insert_photo_24),
            contentDescription = giphyImageItem.title,
            contentScale = ContentScale.FillWidth,
            imageLoader = imageLoader,
        )

        val imageTypeBadgeBackground = MaterialTheme.colorScheme.secondaryContainer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = dimension.minListItemHeight),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = dimension.defaultFullPadding, vertical = dimension.grid_1)
                    .aspectRatio(ratio = 1f)
                    .clip(shape = CircleShape)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .drawBehind {
                        drawRect(color = imageTypeBadgeBackground)
                    },
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                text = giphyImageItem.type.uppercase(),
            )

            if (giphyImageItem.username.isNotBlank()) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = dimension.grid_1)
                        .align(alignment = Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    text = "@${giphyImageItem.username}",
                )
            }

            Spacer(modifier = Modifier.weight(1.0f))

            IconButton(onClick = { onClickToOpen(giphyImageItem.webUrl) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_open_in_browser_24),
                    contentDescription = stringResource(R.string.content_description_open_in_browser),
                    tint = LocalContentColor.current.copy(alpha = 0.68f),
                )
            }

            IconButton(onClick = { onClickToShare(giphyImageItem.webUrl) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                    contentDescription = stringResource(R.string.content_description_copy_image_link),
                    tint = LocalContentColor.current.copy(alpha = 0.68f),
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun GiphyItemPreview(
    @PreviewParameter(GiphyImageItemProvider::class) giphyImageItem: GiphyImageItem,
) {
    GiphyTrendingTheme {
        Surface {
            GiphyItem(
                modifier = Modifier.fillMaxWidth(),
                giphyImageItem = giphyImageItem,
                imageLoader = ImageLoader(LocalContext.current),
                onClickToShare = {},
                onClickToOpen = {},
            )
        }
    }
}
