/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rwmobi.giphytrending.BuildConfig
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsScreen
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsUIEvent
import com.rwmobi.giphytrending.ui.destinations.settings.SettingsViewModel
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingListScreen
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingUIEvent
import com.rwmobi.giphytrending.ui.destinations.trendinglist.TrendingViewModel

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onShowSnackbar: suspend (String) -> Unit,
) {
    NavHost(navController = navController, startDestination = "trendingList") {
        composable(route = "trendingList") {
            val viewModel: TrendingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            TrendingListScreen(
                modifier = modifier,
                onShowSnackbar = onShowSnackbar,
                uiState = uiState,
                uiEvent = TrendingUIEvent(
                    onRefresh = { viewModel.refresh() },
                    onErrorShown = { viewModel.errorShown(it) },
                ),
            )
        }
        composable(route = "settings") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SettingsScreen(
                modifier = modifier,
                apiMinEntries = BuildConfig.API_MIN_ENTRIES.toInt(),
                apiMaxEntries = BuildConfig.API_MAX_ENTRIES.toInt(),
                onShowSnackbar = onShowSnackbar,
                uiState = uiState,
                uiEvent = SettingsUIEvent(
                    onUpdateApiMaxEntries = { viewModel.setApiMax(it) },
                    onErrorShown = { viewModel.errorShown(it) },
                ),
            )
        }
    }
}
