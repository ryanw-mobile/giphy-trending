/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.theme

import android.content.res.Configuration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Source: https://proandroiddev.com/supporting-different-screen-sizes-on-android-with-jetpack-compose-f215c13081bd
 */

class Dimension(
    val grid_0_25: Dp,
    val grid_0_5: Dp,
    val grid_1: Dp,
    val grid_1_5: Dp,
    val grid_2: Dp,
    val grid_2_5: Dp,
    val grid_3: Dp,
    val grid_3_5: Dp,
    val grid_4: Dp,
    val grid_4_5: Dp,
    val grid_5: Dp,
    val grid_5_5: Dp,
    val grid_6: Dp,
    val minTouchTarget: Dp = 48.dp,
    val minListItemHeight: Dp = 52.dp,
    val appBarHeight: Dp = 32.dp,
    val defaultFullPadding: Dp = 16.dp,
    val defaultHalfPadding: Dp = 8.dp,
    val imageButtonSize: Dp = 48.dp,
)

val smallDimension = Dimension(
    grid_0_25 = 1.5f.dp,
    grid_0_5 = 3.dp,
    grid_1 = 6.dp,
    grid_1_5 = 9.dp,
    grid_2 = 12.dp,
    grid_2_5 = 15.dp,
    grid_3 = 18.dp,
    grid_3_5 = 21.dp,
    grid_4 = 24.dp,
    grid_4_5 = 27.dp,
    grid_5 = 30.dp,
    grid_5_5 = 33.dp,
    grid_6 = 36.dp,
)

val sw360Dimension = Dimension(
    grid_0_25 = 2.dp,
    grid_0_5 = 4.dp,
    grid_1 = 8.dp,
    grid_1_5 = 12.dp,
    grid_2 = 16.dp,
    grid_2_5 = 20.dp,
    grid_3 = 24.dp,
    grid_3_5 = 28.dp,
    grid_4 = 32.dp,
    grid_4_5 = 36.dp,
    grid_5 = 40.dp,
    grid_5_5 = 44.dp,
    grid_6 = 48.dp,
)

fun Configuration.getDimension(): Dimension = if (screenWidthDp <= 360) smallDimension else sw360Dimension
