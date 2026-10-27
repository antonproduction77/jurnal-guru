package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NavyPrimary80,
    onPrimary = NavyPrimaryDark,
    primaryContainer = NavyPrimary,
    onPrimaryContainer = Color.White,
    secondary = NavySecondary80,
    onSecondary = NavyPrimaryDark,
    tertiary = Teal80,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9)
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NavySecondaryLight,
    onPrimaryContainer = NavyPrimary,
    secondary = NavySecondary,
    onSecondary = Color.White,
    tertiary = EducationTeal,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderSubtle,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun JurnalGuruTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our signature Navy Blue theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Retain alias for compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    JurnalGuruTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
