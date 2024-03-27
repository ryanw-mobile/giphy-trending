/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.giphytrending.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import uk.ryanwong.giphytrending.R

// Assuming you have font resources under res/font
val francoisOneFontFamily = FontFamily(
    Font(R.font.francois_one),
)

val openSansFontFamily = FontFamily(
    Font(R.font.open_sans_semibold, FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val appTypography = Typography(
    displayLarge = TextStyle(fontFamily = openSansFontFamily),
    displayMedium = TextStyle(fontFamily = openSansFontFamily),
    displaySmall = TextStyle(fontFamily = openSansFontFamily),
    headlineLarge = TextStyle(fontFamily = francoisOneFontFamily),
    headlineMedium = TextStyle(fontFamily = francoisOneFontFamily),
    headlineSmall = TextStyle(fontFamily = francoisOneFontFamily),
    titleLarge = TextStyle(fontFamily = openSansFontFamily),
    titleMedium = TextStyle(fontFamily = openSansFontFamily),
    titleSmall = TextStyle(fontFamily = openSansFontFamily),
    bodyLarge = TextStyle(fontFamily = openSansFontFamily),
    bodyMedium = TextStyle(fontFamily = openSansFontFamily),
    bodySmall = TextStyle(fontFamily = openSansFontFamily),
    labelLarge = TextStyle(fontFamily = openSansFontFamily),
    labelMedium = TextStyle(fontFamily = openSansFontFamily),
    labelSmall = TextStyle(fontFamily = openSansFontFamily),
)
