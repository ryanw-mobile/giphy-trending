/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.giphytrending.R

// Assuming you have font resources under res/font
val openSansCondensedFontFamily = FontFamily(
    Font(R.font.opensans_condensed_light, FontWeight.Light),
    Font(R.font.opensans_condensed_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.opensans_condensed_regular, FontWeight.Normal),
    Font(R.font.opensans_condensed_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.opensans_condensed_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_condensed_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.opensans_condensed_bold, FontWeight.Bold),
    Font(R.font.opensans_condensed_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.opensans_condensed_extrabold, FontWeight.ExtraBold),
    Font(R.font.opensans_condensed_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
)

val openSansFamily = FontFamily(
    Font(R.font.opensans_light, FontWeight.Light),
    Font(R.font.opensans_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.opensans_bold, FontWeight.Bold),
    Font(R.font.opensans_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.opensans_extrabold, FontWeight.ExtraBold),
    Font(R.font.opensans_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
)

val DefaultTypography = Typography()
val appTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = openSansFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = openSansFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = openSansFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = openSansCondensedFontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = openSansCondensedFontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = openSansCondensedFontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = openSansFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = openSansFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = openSansFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = openSansFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = openSansFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = openSansFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = openSansFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = openSansFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = openSansFamily),
)
