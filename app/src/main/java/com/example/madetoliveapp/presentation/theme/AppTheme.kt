package com.example.madetoliveapp.presentation.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definir los colores claros y oscuros
private val DarkColorPalette = darkColors(
    primary = Color(0xFF522258),
    primaryVariant = Color(0xFF8C3061),
    secondary = Color(0xFFC63C51),
    secondaryVariant = Color(0xFFD95F59)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF522258),
    primaryVariant = Color(0xFF8C3061),
    secondary = Color(0xFFC63C51),
    secondaryVariant = Color(0xFFD95F59)
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
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}