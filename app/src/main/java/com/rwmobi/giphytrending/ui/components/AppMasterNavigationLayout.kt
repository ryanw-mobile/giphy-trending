/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.navigation.AppNavHost
import com.rwmobi.giphytrending.ui.navigation.AppNavItem
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.utils.getPreviewWindowSizeClass

private enum class NavigationLayoutType {
    BottomNavigation,
    NavigationRail,
    FullScreen,
}

private fun calculateNavigationLayout(windowWidthSizeClass: WindowWidthSizeClass): NavigationLayoutType {
    return when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            NavigationLayoutType.BottomNavigation
        }

        else -> {
            // WindowWidthSizeClass.Medium, -- tablet portrait
            // WindowWidthSizeClass.Expanded, -- phone landscape mode
            NavigationLayoutType.NavigationRail
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMasterNavigationLayout(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppNavItem?>(null) }
    val navigationLayoutType = calculateNavigationLayout(windowWidthSizeClass = windowSizeClass.widthSizeClass)

    Row(modifier = modifier) {
        if (navigationLayoutType == NavigationLayoutType.NavigationRail) {
            AppNavigationRail(
                modifier = Modifier.fillMaxHeight(),
                navController = navController,
                onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
            )

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
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
            bottomBar = {
                if (navigationLayoutType == NavigationLayoutType.BottomNavigation) {
                    Column {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                        )

                        AppBottomNavigationBar(
                            navController = navController,
                            onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
                        )
                    }
                }
            },
        ) { paddingValues ->
            // Note that we take MaxSize and expect individual screens to handle screen size
            val actionLabel = stringResource(android.R.string.ok)
            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                isLargeScreen = navigationLayoutType == NavigationLayoutType.NavigationRail,
                navController = navController,
                lastDoubleTappedNavItem = lastDoubleTappedNavItem.value,
                scrollBehavior = scrollBehavior,
                onShowSnackbar = { errorMessageText ->
                    snackbarHostState.showSnackbar(
                        message = errorMessageText,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Long,
                    )
                },
                onScrolledToTop = { lastDoubleTappedNavItem.value = null },
            )
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun Preview() {
    GiphyTrendingTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            AppMasterNavigationLayout(
                modifier = Modifier.fillMaxSize(),
                windowSizeClass = getPreviewWindowSizeClass(),
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}
