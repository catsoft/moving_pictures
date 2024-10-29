package com.example.movingPictures.ui.theme.themes

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
import com.example.movingPictures.ui.theme.colors.*
import com.example.movingPictures.ui.theme.colors.ColorPalette
import com.example.movingPictures.ui.theme.typography.MPTypography

private val lightScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    primaryContainer = White,
    onPrimaryContainer = Black,
    secondary = Green,
    onSecondary = White,
    secondaryContainer = Black,
    onSecondaryContainer = White,
    tertiary = Pink80,
    onTertiary = Black,
    tertiaryContainer = White,
    onTertiaryContainer = Black,
    error = Red,
    onError = White,
    errorContainer = White,
    onErrorContainer = Red,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = Gray,
    onSurfaceVariant = White,
    outline = Gray,
    outlineVariant = Gray,
    scrim = Black,
    inverseSurface = Black,
    inverseOnSurface = White,
    inversePrimary = Blue,
    surfaceDim = Gray,
    surfaceBright = White,
    surfaceContainerLowest = White,
    surfaceContainerLow = Gray,
    surfaceContainer = White,
    surfaceContainerHigh = White,
    surfaceContainerHighest = White,
)


private val darkScheme = darkColorScheme(
    primary = Blue,
    onPrimary = Black,
    primaryContainer = Black,
    onPrimaryContainer = White,
    secondary = Green,
    onSecondary = Black,
    secondaryContainer = Black,
    onSecondaryContainer = White,
    tertiary = Pink80,
    onTertiary = White,
    tertiaryContainer = Black,
    onTertiaryContainer = White,
    error = Red,
    onError = Black,
    errorContainer = Black,
    onErrorContainer = White,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White,
    surfaceVariant = Gray,
    onSurfaceVariant = Black,
    outline = Gray,
    outlineVariant = Gray,
    scrim = Black,
    inverseSurface = White,
    inverseOnSurface = Black,
    inversePrimary = Blue,
    surfaceDim = Gray,
    surfaceBright = Black,
    surfaceContainerLowest = Black,
    surfaceContainerLow = Gray,
    surfaceContainer = Black,
    surfaceContainerHigh = Black,
    surfaceContainerHighest = Black,
)


@Composable
fun MPTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkTheme -> darkScheme
        else -> lightScheme
    }

    val colorPalette = when {
        isDarkTheme -> DarkPalette
        else -> LightPalette
    }
    ColorPalette.setupPalette(colorPalette)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.onPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MPTypography,
        content = content
    )
}