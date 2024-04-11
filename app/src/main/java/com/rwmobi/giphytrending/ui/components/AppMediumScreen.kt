package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMediumScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    Row(modifier = modifier) {
        AppNavigationRail(
            modifier = Modifier.fillMaxHeight(),
            navController = navController,
        )
        // Other content
        Text(text = "Medium Screen")
    }
}
