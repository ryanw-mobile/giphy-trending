/*
 * Copyright 2024-2026 RW MobiMedia UK Limited
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.test

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.rwmobi.giphytrending.MainActivity

typealias GiphyTrendingTestRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
