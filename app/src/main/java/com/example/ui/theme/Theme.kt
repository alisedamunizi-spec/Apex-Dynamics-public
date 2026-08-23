package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GeometricBalanceColorScheme = lightColorScheme(
    primary = GeoPrimary,
    onPrimary = Color.White,
    primaryContainer = GeoPrimaryContainer,
    onPrimaryContainer = GeoOnPrimaryContainer,
    secondary = GeoCyanAccent,
    onSecondary = Color.White,
    secondaryContainer = GeoSurfaceElevated,
    onSecondaryContainer = GeoPrimary,
    tertiary = GeoPurpleAI,
    onTertiary = Color.White,
    tertiaryContainer = GeoSurfaceVariant,
    onTertiaryContainer = GeoPurpleAI,
    background = GeoBackground,
    onBackground = TextPrimary,
    surface = GeoSurface,
    onSurface = TextPrimary,
    surfaceVariant = GeoSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CardBorderColor,
    outlineVariant = CardBorderColor.copy(alpha = 0.5f)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GeometricBalanceColorScheme,
        typography = Typography,
        content = content
    )
}

