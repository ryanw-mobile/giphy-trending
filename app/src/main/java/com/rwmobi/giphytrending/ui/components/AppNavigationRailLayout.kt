package com.rwmobi.giphytrending.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Row(modifier = modifier) {
        AppNavigationRail(
            modifier = Modifier.fillMaxHeight(),
            navController = navController,
        )

        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    modifier = Modifier.wrapContentHeight(),
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
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
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.surface,
            ) {
                // Note that we take MaxSize and expect individual screens to handle screen size
                val actionLabel = stringResource(android.R.string.ok)
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    navController = navController,
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
}

@Preview(
    name = "Phone - Landscape",
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
)
@Composable
private fun AppNavigationRailLayoutPreview() {
    GiphyTrendingTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            AppNavigationRailLayout(
                modifier = Modifier.fillMaxSize(),
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}
