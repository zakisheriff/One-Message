package com.theoneatom.onemessage.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// One Message uses a single dark theme for brand consistency
private val OneMessageColorScheme = darkColorScheme(
    // Primary
    primary = OneMessageColors.White,
    onPrimary = OneMessageColors.Black,
    primaryContainer = OneMessageColors.SurfaceVariant,
    onPrimaryContainer = OneMessageColors.White,
    
    // Secondary
    secondary = OneMessageColors.AccentDark,
    onSecondary = OneMessageColors.Black,
    secondaryContainer = OneMessageColors.SurfaceVariant,
    onSecondaryContainer = OneMessageColors.White,
    
    // Tertiary
    tertiary = OneMessageColors.TextMuted,
    onTertiary = OneMessageColors.Black,
    
    // Background
    background = OneMessageColors.Background,
    onBackground = OneMessageColors.TextPrimary,
    
    // Surface
    surface = OneMessageColors.Surface,
    onSurface = OneMessageColors.TextPrimary,
    surfaceVariant = OneMessageColors.SurfaceVariant,
    onSurfaceVariant = OneMessageColors.TextSecondary,
    
    // Outline
    outline = OneMessageColors.GlassBorder,
    outlineVariant = OneMessageColors.GlassBg,
    
    // Error
    error = OneMessageColors.Error,
    onError = OneMessageColors.White,
    
    // Inverse
    inverseSurface = OneMessageColors.White,
    inverseOnSurface = OneMessageColors.Black,
    inversePrimary = OneMessageColors.Black,
)

@Composable
fun OneMessageTheme(
    // Always use dark theme for One Message brand identity
    darkTheme: Boolean = true,
    // Disable dynamic color for consistent branding
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = OneMessageColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar to match our dark theme
            window.statusBarColor = OneMessageColors.Background.toArgb()
            window.navigationBarColor = OneMessageColors.Background.toArgb()
            // Use light icons on dark background
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}