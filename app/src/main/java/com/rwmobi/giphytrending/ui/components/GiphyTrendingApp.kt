/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun GiphyTrendingApp(
    windowSizeClass: WindowSizeClass,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    GiphyTrendingTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            // Select a navigation element based on window size.
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    AppBottomNavigationLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding(),
                        windowSizeClass = windowSizeClass,
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                    )
                }

                WindowWidthSizeClass.Medium, // tablet portrait
                WindowWidthSizeClass.Expanded, // phone landscape mode
                -> {
                    AppNavigationRailLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding(),
                        windowSizeClass = windowSizeClass,
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    }
}
