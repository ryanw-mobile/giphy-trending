/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsScreen
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsUIEvent
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingListScreen
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingUIEvent
import com.rwmobi.giphytrending.ui.viewmodel.SettingsViewModel
import com.rwmobi.giphytrending.ui.viewmodel.TrendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    windowsSizeClass: WindowSizeClass,
    onShowSnackbar: suspend (String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "trendingList",
    ) {
        composable(route = "trendingList") {
            val viewModel: TrendingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            TrendingListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                windowSizeClass = windowsSizeClass,
                onShowSnackbar = onShowSnackbar,
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = TrendingUIEvent(
                    onRefresh = { viewModel.refresh() },
                    onErrorShown = { viewModel.errorShown(it) },
                ),
            )

            // Reset the scroll behavior when this composable enters
            LaunchedEffect(Unit) {
                scrollBehavior.state.heightOffset = 0f
                scrollBehavior.state.contentOffset = 0f
            }
        }
        composable(route = "settings") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SettingsScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                windowSizeClass = windowsSizeClass,
                apiMinEntries = BuildConfig.API_MIN_ENTRIES.toInt(),
                apiMaxEntries = BuildConfig.API_MAX_ENTRIES.toInt(),
                onShowSnackbar = onShowSnackbar,
                uiState = uiState,
                uiEvent = SettingsUIEvent(
                    onUpdateApiMaxEntries = { viewModel.setApiRequestLimit(it) },
                    onUpdateRating = { viewModel.setRating(it) },
                    onErrorShown = { viewModel.errorShown(it) },
                ),
            )

            // Reset the scroll behavior when this composable enters
            LaunchedEffect(Unit) {
                scrollBehavior.state.heightOffset = 0f
                scrollBehavior.state.contentOffset = 0f
            }
        }
    }
}