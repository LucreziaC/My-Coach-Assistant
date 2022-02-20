package com.lucreziacarena.mycoachassistant.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String){
    object Athletes : NavigationItem("athletes", Icons.Filled.DirectionsRun, "Athletes")
    object Leaderboard : NavigationItem("leaderboard", Icons.Filled.Leaderboard, "Leaderboard")
}
