package com.example.madetoliveapp.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.madetoliveapp.R

val CabinSketch = FontFamily(
    Font(R.font.rubik, weight = FontWeight.Bold)
)

val CottageTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 24.sp,
        color = DarkText
    ),
    headlineSmall = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 20.sp,
        color = DarkText
    ),
    bodyLarge = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 16.sp,
        color = DarkText
    ),
    labelLarge = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 14.sp,
        color = DarkText
    ),
    titleLarge = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 22.sp,
        color = DarkText
    ),
    titleMedium = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 18.sp,
        color = DarkText
    ),
    titleSmall = TextStyle(
    ),
    labelMedium = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 14.sp,
        color = DarkText,
        fontWeight = FontWeight.Bold
    ),
    bodySmall = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 12.sp,
        color = DarkText
    ),
    // Add more styles as needed
)