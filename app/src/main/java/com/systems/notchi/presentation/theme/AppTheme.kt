package com.systems.notchi.presentation.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary Range – Soft Blue Gray
val MistBlue = Color(0xFFAEC6CF)        // Soft blue-gray
val MistBlueDark = Color(0xFF7A9BAA)    // Deeper cool blue
val MistBlueLight = Color(0xFFDCE9ED)

val MistGray = Color(0xFFBFC9CE)        // Less saturated blue-gray
val MistGrayDark = Color(0xFF8D999F)    // Muted deeper cool gray
val MistGrayLight = Color(0xFFE4E8EB)// Very light hint of blue

// Secondary – Light Steel Gray
val Steel = Color(0xFFB0BEC5)
val SteelDark = Color(0xFF78909C)
val SteelLight = Color(0xFFCFD8DC)

val StoneGray = Color(0xFFC1C9CD)       // Desaturated steel gray
val StoneDark = Color(0xFF90989D)
val StoneLight = Color(0xFFD9DEE1)

// Accent – Soft Indigo (optional use)
val SoftIndigo = Color(0xFF9FA8DA)
val IndigoDark = Color(0xFF5C6BC0)

val DustyIndigo = Color(0xFFA6ABC4)     // Softer, more gray-toned indigo
val IndigoMutedDark = Color(0xFF6E7591)

//Neutrals
val LightGray = Color(0xFFF5F5F5)
val CoolWhite = Color(0xFFFAFAFA)
val Cloud = Color(0xFFECEFF1)
val DarkText = Color(0xFF2E2E2E)
val CharcoalText = Color(0xFF2F2F2F)
val SubtleText = Color(0xFF4E5D6C)

// Failures
val SoftError = Color(0xFFFCEDED)
val ErrorTextCool = Color(0xFF8B1E1E)

//Tasks background
val PositiveTaskUnchecked = Color(0xFFF8F9FA)
val PositiveTaskChecked =  Color(0xFFE9F5EC)
val FailTaskUnchecked = Color(0xFFDADADA)   // dusty light gray
val FailTaskChecked = Color(0xFF9EA4A6)

//Tasks border
val PositiveTaskUncheckedAccent = Color(0xFFE0E0E0)
val PositiveTaskCheckedAccent =  Color(0xFFAED9C5)
val FailTaskUncheckedAccent = Color(0xFF7A7A7A)  // muted charcoal (cooler than brown)
val FailTaskCheckedAccent = Color(0xFF5A5F60)

private val LightColorScheme = lightColorScheme(
    primary = MistGray,
    onPrimary = Color.White,

    secondary = StoneGray,
    onSecondary = Color.White,

    tertiary = DustyIndigo,

    background = CoolWhite,
    onBackground = CharcoalText,

    surface = LightGray,
    onSurface = SubtleText,
    surfaceVariant = MistBlueLight,

    error = SoftError,
    onError = ErrorTextCool
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