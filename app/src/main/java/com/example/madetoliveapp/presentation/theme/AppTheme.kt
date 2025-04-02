package com.example.madetoliveapp.presentation.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Your cozy colors
val EarthBrown = Color(0xFF6B4F3B)
val DeepBrown = Color(0xFF5A3E2B)
val Black = Color(0xFF000000)
val MossGreen = Color(0xFF76885B)
val ForestGreen = Color(0xFF4C5B3A)
val PaperBeige = Color(0xFFF4E4C1)
val Cream = Color(0xFFFDF6EC)

private val LightColorScheme = lightColorScheme(
    primary = EarthBrown,
    onPrimary = Cream,
    secondary = MossGreen,
    onSecondary = Cream,
    background = PaperBeige,
    onBackground = DeepBrown,
    surface = Cream,
    onSurface = Black
)
@Composable
fun MadeToLiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = CottageTypography,
        shapes = CottageShapes,
        content = content
    )
}