/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.navigation.AppNavItem
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppNavItem) -> Unit,
) {
    val navBarContentDescription = stringResource(R.string.content_description_navigation_bar)

    NavigationBar(
        modifier = modifier.semantics {
            contentDescription = navBarContentDescription
        },
        tonalElevation = 0.dp,
        containerColor = GiphyTrendingTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        for (item in AppNavItem.navigationBarItems) {
            val selected = currentRoute == item.screenRoute
            val navBarItemContentDescription = stringResource(item.titleResId)

            NavigationBarItem(
                modifier = Modifier.semantics {
                    contentDescription = navBarItemContentDescription
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.screenRoute) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    } else {
                        onCurrentRouteSecondTapped(item)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = if (selected) item.iconFocusedResId else item.iconDefaultResId),
                        contentDescription = null,
                        tint = if (selected) GiphyTrendingTheme.colorScheme.tertiary else LocalContentColor.current,
                    )
                },
                label = {
                    AnimatedVisibility(visible = selected) {
                        Text(
                            text = stringResource(id = item.titleResId).uppercase(),
                            style = GiphyTrendingTheme.typography.labelMedium,
                        )
                    }
                },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    GiphyTrendingTheme {
        Surface {
            AppBottomNavigationBar(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                navController = rememberNavController(),
                onCurrentRouteSecondTapped = {},
            )
        }
    }
}
