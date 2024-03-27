/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.giphytrending.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LightPurple,
    onPrimary = DarkerPurple,
    primaryContainer = PinkishWhite,
    onPrimaryContainer = DarkerPurple,
    inversePrimary = DeepPurple,
    secondary = LighterPurple,
    onSecondary = MediumGrey, // Adjusted for color contrast and readability
    secondaryContainer = GreyPink, // Adjusted for matching appearance
    onSecondaryContainer = SoftPink,
    tertiary = LightestPurple,
    onTertiary = DarkPurple, // Adjusted for readability
    tertiaryContainer = MediumGrey, // Adjusted for matching appearance
    onTertiaryContainer = LightPink,
    background = NearlyBlack,
    onBackground = SoftGrey,
    surface = NearlyBlack,
    onSurface = SoftGrey,
    surfaceVariant = MediumGrey,
    onSurfaceVariant = DarkPinkishGrey,
    surfaceTint = LightPurple,
    inverseSurface = SoftGrey,
    inverseOnSurface = NearlyBlack,
    error = LightRed,
    onError = DarkPurple, // Adjusted for readability
    errorContainer = DeepRed,
    onErrorContainer = LightRed,
    outline = DarkerGrey,
)

private val LightColorScheme = lightColorScheme(
    primary = DeepPurple,
    onPrimary = White,
    primaryContainer = LightPink,
    onPrimaryContainer = DarkPurple,
    inversePrimary = LightPurple,
    secondary = MutedPurple,
    onSecondary = White,
    secondaryContainer = SoftPink,
    onSecondaryContainer = DarkPurple, // Adjusted for readability
    tertiary = LightestPurple,
    onTertiary = White,
    tertiaryContainer = LightPink, // Adjusted for color contrast and readability
    onTertiaryContainer = DarkPurple, // Adjusted for readability
    background = VeryLightGrey,
    onBackground = DarkGrey,
    surface = VeryLightGrey,
    onSurface = DarkGrey,
    surfaceVariant = GreyPink,
    onSurfaceVariant = MediumGrey,
    surfaceTint = DeepPurple,
    inverseSurface = AlmostBlack,
    inverseOnSurface = LightGrey,
    error = DeepRed,
    onError = White,
    errorContainer = LightRed,
    onErrorContainer = DarkPurple,
    outline = DustyPurple,
)

@Composable
fun GiphyTrendingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
