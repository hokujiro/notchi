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
        color = DeepBrown
    ),
    bodyLarge = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 16.sp,
        color = EarthBrown
    ),
    labelLarge = TextStyle(
        fontFamily = CabinSketch,
        fontSize = 14.sp,
        color = DeepBrown
    )
    // Add more styles as needed
)