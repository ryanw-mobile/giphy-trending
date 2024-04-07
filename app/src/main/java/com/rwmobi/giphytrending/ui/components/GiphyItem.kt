/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.ui.theme.getDimension

@Composable
fun GiphyItem(
    modifier: Modifier = Modifier,
    giphyImageItem: GiphyImageItem,
    onClickToOpen: (String) -> Unit,
    onClickToShare: (String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            text = giphyImageItem.title,
        )

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(giphyImageItem.previewUrl)
                .crossfade(true)
                .build(),
            fallback = painterResource(R.drawable.ic_baseline_error_outline_24),
            error = painterResource(R.drawable.ic_baseline_error_outline_24),
            placeholder = painterResource(R.drawable.ic_baseline_error_outline_24),
            contentDescription = giphyImageItem.title,
            contentScale = ContentScale.FillWidth,
            imageLoader = ImageLoader.Builder(LocalContext.current.applicationContext)
                .components {
                    if (android.os.Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = dimension.minListItemHeight),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = dimension.defaultFullPadding)
                    .drawBehind {
                        drawRect(color = Color.Black)
                    },
                style = MaterialTheme.typography.labelMedium,
                text = giphyImageItem.type,
            )

            if (giphyImageItem.username.isNotBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = dimension.defaultFullPadding)
                        .drawBehind {
                            drawRect(color = Color.Black)
                        },
                    style = MaterialTheme.typography.labelMedium,
                    text = giphyImageItem.username,
                )
            }

            Spacer(modifier = Modifier.weight(1.0f))

            IconButton(onClick = { onClickToOpen(giphyImageItem.webUrl) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_open_in_browser_24),
                    contentDescription = stringResource(R.string.content_description_open_in_browser),
                )
            }

            IconButton(onClick = { onClickToShare(giphyImageItem.webUrl) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                    contentDescription = stringResource(R.string.content_description_copy_image_link),
                )
            }
        }
    }
}
