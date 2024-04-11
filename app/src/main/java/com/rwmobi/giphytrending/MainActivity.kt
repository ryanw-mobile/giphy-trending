/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.rwmobi.giphytrending.ui.components.AppBottomNavigationLayout
import com.rwmobi.giphytrending.ui.components.AppNavigationRailLayout
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            GiphyTrendingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    // Select a navigation element based on window size.
                    when (widthSizeClass) {
                        WindowWidthSizeClass.Compact -> {
                            AppBottomNavigationLayout(
                                modifier = Modifier.fillMaxSize(),
                                navController = navController,
                                snackbarHostState = snackbarHostState,
                            )
                        }

                        WindowWidthSizeClass.Medium, // tablet portrait
                        WindowWidthSizeClass.Expanded, // phone landscape mode
                        -> {
                            AppNavigationRailLayout(
                                modifier = Modifier.fillMaxSize(),
                                navController = navController,
                                snackbarHostState = snackbarHostState,
                            )
                        }
                    }
                }
            }
        }
    }
}
