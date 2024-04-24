/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.ui.destinations.search.SearchScreen
import com.rwmobi.giphytrending.ui.destinations.search.SearchUIEvent
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsScreen
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsUIEvent
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingListScreen
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingUIEvent
import com.rwmobi.giphytrending.ui.viewmodel.SearchViewModel
import com.rwmobi.giphytrending.ui.viewmodel.SettingsViewModel
import com.rwmobi.giphytrending.ui.viewmodel.TrendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    lastDoubleTappedNavItem: AppNavItem?,
    scrollBehavior: TopAppBarScrollBehavior,
    windowSizeClass: WindowSizeClass,
    onShowSnackbar: suspend (String) -> Unit,
    onScrolledToTop: (AppNavItem) -> Unit,
) {
    fun resetScrollBehavior() {
        scrollBehavior.state.heightOffset = 0f
        scrollBehavior.state.contentOffset = 0f
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "trendingList",
    ) {
        composable(route = "trendingList") {
            val viewModel: TrendingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.TrendingList) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
                if (enabled) {
                    resetScrollBehavior()
                }
            }

            TrendingListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                useCardLayout = (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact),
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = TrendingUIEvent(
                    onRefresh = { viewModel.refresh() },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.TrendingList) },
                    onShowSnackbar = onShowSnackbar,
                ),
            )

            // Reset the scroll behavior when this composable enters
            LaunchedEffect(Unit) {
                resetScrollBehavior()
            }
        }

        composable(route = "search") {
            val viewModel: SearchViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Search) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
                if (enabled) {
                    resetScrollBehavior()
                }
            }

            SearchScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                useCardLayout = (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact),
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = SearchUIEvent(
                    onClearKeyword = { viewModel.clearKeyword() },
                    onUpdateKeyword = { viewModel.updateKeyword(it) },
                    onSearch = { viewModel.search() },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.Search) },
                    onShowSnackbar = onShowSnackbar,
                ),
            )

            // Reset the scroll behavior when this composable enters
            LaunchedEffect(Unit) {
                resetScrollBehavior()
            }
        }

        composable(route = "settings") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Settings) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
                if (enabled) {
                    resetScrollBehavior()
                }
            }

            SettingsScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                windowSizeClass = windowSizeClass,
                apiMinEntries = BuildConfig.API_MIN_ENTRIES.toInt(),
                apiMaxEntries = BuildConfig.API_MAX_ENTRIES.toInt(),
                uiState = uiState,
                uiEvent = SettingsUIEvent(
                    onUpdateApiMaxEntries = { viewModel.setApiRequestLimit(it) },
                    onUpdateRating = { viewModel.setRating(it) },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.Settings) },
                    onShowSnackbar = onShowSnackbar,
                ),
            )

            // Reset the scroll behavior when this composable enters
            LaunchedEffect(Unit) {
                resetScrollBehavior()
            }
        }
    }
}
