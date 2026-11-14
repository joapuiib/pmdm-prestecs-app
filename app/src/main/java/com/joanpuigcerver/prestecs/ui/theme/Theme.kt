package com.joanpuigcerver.prestecs.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Opcions del tema de l'aplicació. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

// Colors d'accent de l'aplicació de préstecs, perquè l'aspecte siga el mateix.
private val LightColors = lightColorScheme(
    primary = Color(0xFF1F5FBF),
    onPrimary = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF6EA8FE),
    onPrimary = Color(0xFF0B1A33),
)

/**
 * Tema de l'aplicació, clar o fosc.
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
@Composable
fun PrestecsTheme(themeMode: ThemeMode = ThemeMode.SYSTEM, content: @Composable () -> Unit) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, content = content)
}
