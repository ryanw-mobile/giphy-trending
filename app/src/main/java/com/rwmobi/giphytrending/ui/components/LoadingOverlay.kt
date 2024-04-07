/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
) {
    val overlayColor = MaterialTheme.colorScheme.inverseSurface
    Box(
        modifier = modifier.drawBehind {
            drawRect(color = overlayColor.copy(alpha = 0.32f))
        },
    ) {
        // Block touch events
        Surface(modifier = Modifier.fillMaxSize()) {}

        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@PreviewLightDark
@Composable
private fun LoadingOverlayPreview() {
    GiphyTrendingTheme {
        Surface {
            LoadingOverlay(modifier = Modifier.fillMaxSize())
        }
    }
}
