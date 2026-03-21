/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun GiphyTrendingApp(
    adaptiveInfo: WindowAdaptiveInfo,
    imageLoader: ImageLoader,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    GiphyTrendingTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = GiphyTrendingTheme.colorScheme.background,
        ) {
            AppMasterNavigationLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                adaptiveInfo = adaptiveInfo,
                navController = navController,
                imageLoader = imageLoader,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}
