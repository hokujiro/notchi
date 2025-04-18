package com.example.madetoliveapp.presentation.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary Range – Refined Cocoa
val CocoaPrimary = Color(0xFF8B6A5C)
val CocoaDark = Color(0xFF5E4035)
val CocoaLight = Color(0xFFD2B6A8)

// Secondary Range – Muted Sage Green (calming, contrast)
val Sage = Color(0xFF889A6C)
val SageDark = Color(0xFF566F64)
val SageLight = Color(0xFFBFD4C9)

// Accent – Deep Blush (used sparingly for highlights or selected states)
val BlushAccent = Color(0xFFE6A4A6)
val BlushDark = Color(0xFFAD7073)

// Neutrals
val SoftNeutral = Color(0xFFE0E0E0)
val LightPaper = Color(0xFFF8F9FA)
val DimmedIvory = Color(0xFFE7DED6)
val SurfaceWhite = Color(0xFFFFFEFD)
val DarkText = Color(0xFF2E2E2E)
val SurfaceText = Color(0xFF3A2D28)
val WarmIvory = Color(0xFFF8F4EF)

// Failures
val ElegantRed = Color(0xFFFCE8E8)
val ErrorText = Color(0xFF7A1F1F)
val ErrorBackground = Color(0xFFECC4C4)

private val LightColorScheme = lightColorScheme(
    primary = CocoaPrimary,              // Refined neutral anchor
    onPrimary = Color.White,

    secondary = Sage,                    // Calming green for chips, headers
    onSecondary = Color.White,

    tertiary = BlushAccent,              // Soft accent color (used sparingly)

    background = WarmIvory,                // Slightly deeper ivory for warmth
    onBackground = DarkText,

    surface = SurfaceWhite,              // Clean card and component surfaces
    onSurface = SurfaceText,

    error = ErrorBackground,             // Soft red for background
    onError = ErrorText
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