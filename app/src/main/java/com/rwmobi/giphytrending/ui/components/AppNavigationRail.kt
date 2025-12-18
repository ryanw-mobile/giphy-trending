/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
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
import java.util.Locale

@Composable
fun AppNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppNavItem) -> Unit,
) {
    val contentDescriptionNavigationRail = stringResource(R.string.content_description_navigation_rail)

    NavigationRail(
        modifier = modifier.semantics {
            contentDescription = contentDescriptionNavigationRail
        },
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Spacer(Modifier.weight(1f))

        for (item in AppNavItem.navigationBarItems) {
            val selected = currentRoute == item.screenRoute
            val navRailItemContentDescription = stringResource(item.titleResId)

            NavigationRailItem(
                modifier = Modifier
                    .padding(vertical = GiphyTrendingTheme.dimens.defaultFullPadding)
                    .semantics { contentDescription = navRailItemContentDescription },
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
                    // Always show label to maintain the vertical position
                    Text(
                        text = stringResource(id = item.titleResId).replaceFirstChar {
                            if (it.isLowerCase()) {
                                it.titlecase(locale = Locale.ENGLISH)
                            } else {
                                it.toString()
                            }
                        },
                        style = GiphyTrendingTheme.typography.labelMedium,
                    )
                },
            )
        }

        Spacer(Modifier.weight(1f))
    }
}

@PreviewLightDark
@Composable
private fun NavigationRailPreview() {
    GiphyTrendingTheme {
        Surface {
            AppNavigationRail(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                navController = rememberNavController(),
                onCurrentRouteSecondTapped = {},
            )
        }
    }
}
