/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.navigation.AppNavHost
import com.rwmobi.giphytrending.ui.navigation.AppNavItem
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.utils.getPreviewWindowAdaptiveInfo
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
@Composable
fun AppMasterNavigationLayout(
    modifier: Modifier = Modifier,
    adaptiveInfo: WindowAdaptiveInfo,
    navController: NavHostController,
    imageLoader: ImageLoader,
    snackbarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppNavItem?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationSuiteScaffold(
        modifier = modifier,
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo),
        navigationSuiteItems = {
            AppNavItem.navigationBarItems.forEach { item ->
                val selected = currentRoute == item.screenRoute
                item(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(item.screenRoute) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        } else {
                            lastDoubleTappedNavItem.value = item
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = if (selected) item.iconFocusedResId else item.iconDefaultResId),
                            contentDescription = stringResource(id = item.titleResId),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.titleResId).replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString()
                            },
                        )
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.wrapContentHeight(),
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = GiphyTrendingTheme.colorScheme.background,
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
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            val actionLabel = stringResource(android.R.string.ok)
            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                isLargeScreen = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo).toString() != "NavigationBar",
                navController = navController,
                imageLoader = imageLoader,
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
            modifier = Modifier.fillMaxSize(),
            color = GiphyTrendingTheme.colorScheme.surface,
        ) {
            AppMasterNavigationLayout(
                modifier = Modifier.fillMaxSize(),
                adaptiveInfo = getPreviewWindowAdaptiveInfo(),
                imageLoader = ImageLoader(androidx.compose.ui.platform.LocalContext.current),
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}
