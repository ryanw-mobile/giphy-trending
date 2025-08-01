/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun GiphyTrendingApp(
    windowSizeClass: WindowSizeClass,
    imageLoader: ImageLoader,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    GiphyTrendingTheme {
        Surface(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize(),
            color = GiphyTrendingTheme.colorScheme.background,
        ) {
            AppMasterNavigationLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                windowSizeClass = windowSizeClass,
                navController = navController,
                imageLoader = imageLoader,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}
