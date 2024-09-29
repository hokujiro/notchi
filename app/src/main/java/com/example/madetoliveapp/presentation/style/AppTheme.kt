package com.example.madetoliveapp.presentation.style


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definir los colores claros y oscuros
private val DarkColorPalette = darkColors(
    primary = Color(0x522258),
    primaryVariant = Color(0x8C3061),
    secondary = Color(0xC63C51),
    secondaryVariant = Color(0xD95F59)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFFC5C5),
    primaryVariant = Color(0xFFEBD8),
    secondary = Color(0xC7DCA7),
    secondaryVariant = Color(0x89B9AD)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}