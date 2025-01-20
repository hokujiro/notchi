package com.example.madetoliveapp.presentation.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()) // Convert to system's default time zone
        .toLocalDate() // Extract LocalDate
}