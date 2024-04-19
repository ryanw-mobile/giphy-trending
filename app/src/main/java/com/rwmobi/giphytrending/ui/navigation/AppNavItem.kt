/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rwmobi.giphytrending.R

sealed class AppNavItem(var screenRoute: String, @StringRes var titleResId: Int, @DrawableRes var iconDefaultResId: Int, @DrawableRes var iconFocusedResId: Int) {

    data object TrendingList : AppNavItem(
        titleResId = R.string.trending,
        iconDefaultResId = R.drawable.outline_local_fire_department_24,
        iconFocusedResId = R.drawable.baseline_local_fire_department_24,
        screenRoute = "trendingList",
    )

    data object Search : AppNavItem(
        titleResId = R.string.search,
        iconDefaultResId = R.drawable.outline_search_24,
        iconFocusedResId = R.drawable.sharp_saved_search_24,
        screenRoute = "search",
    )

    data object Settings : AppNavItem(
        titleResId = R.string.settings,
        iconDefaultResId = R.drawable.outline_settings_24,
        iconFocusedResId = R.drawable.baseline_settings_suggest_24,
        screenRoute = "settings",
    )

    companion object {
        val allItems: List<AppNavItem>
            get() = listOf(TrendingList, Search, Settings)
    }
}
