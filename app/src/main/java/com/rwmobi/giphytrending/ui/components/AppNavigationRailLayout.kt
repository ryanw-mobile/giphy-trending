/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.navigation.NavHost
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationRailLayout(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Row(modifier = modifier) {
        AppNavigationRail(
            modifier = Modifier.fillMaxHeight(),
            navController = navController,
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
        )

        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    modifier = Modifier.wrapContentHeight(),
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    scrollBehavior = scrollBehavior,
                    title = {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            alignment = Alignment.Center,
                            painter = painterResource(R.drawable.app_branding),
                            contentDescription = stringResource(id = R.string.app_name),
                        )
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                )
            },
        ) { paddingValues ->
            // Note that we take MaxSize and expect individual screens to handle screen size
            val actionLabel = stringResource(android.R.string.ok)
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                windowsSizeClass = windowSizeClass,
                navController = navController,
                scrollBehavior = scrollBehavior,
                onShowSnackbar = { errorMessageText ->
                    snackbarHostState.showSnackbar(
                        message = errorMessageText,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Long,
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview(
    name = "Phone - Landscape Light",
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Phone - Landscape Dark",
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun Preview() {
    GiphyTrendingTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            AppNavigationRailLayout(
                modifier = Modifier.fillMaxSize(),
                windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}