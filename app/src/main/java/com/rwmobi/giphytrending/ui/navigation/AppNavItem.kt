/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.navigation

import com.rwmobi.giphytrending.R

sealed class AppNavItem(var screenRoute: String, var titleResId: Int, var iconResId: Int) {

    data object TrendingList : AppNavItem(titleResId = R.string.trending, iconResId = R.drawable.ic_baseline_local_fire_department_24, screenRoute = "trendingList")
    data object Settings : AppNavItem(titleResId = R.string.settings, iconResId = R.drawable.outline_app_settings_alt_24, screenRoute = "settings")

    companion object {
        val allItems: List<AppNavItem>
            get() = listOf(TrendingList, Settings)
    }
}
