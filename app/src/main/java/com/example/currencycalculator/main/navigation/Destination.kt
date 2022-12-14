package com.example.currencycalculator.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Transform
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val route: String, val label: String, val icon: ImageVector) {
    Exchange("exchange", "Exchange Rate", Icons.Default.Transform),
    Settings("settings", "Settings", Icons.Default.Settings)
}