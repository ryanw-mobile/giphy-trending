/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.models

import com.rwmobi.giphytrending.R

sealed class BottomNavItem(var screenRoute: String, var titleResId: Int, var iconResId: Int) {

    data object TrendingList : BottomNavItem(titleResId = R.string.trending, iconResId = R.drawable.ic_baseline_local_fire_department_24, screenRoute = "trendingList")
    data object Settings : BottomNavItem(titleResId = R.string.settings, iconResId = R.drawable.outline_app_settings_alt_24, screenRoute = "settings")

    companion object {
        val allItems: List<BottomNavItem>
            get() = listOf(TrendingList, Settings)
    }
}
