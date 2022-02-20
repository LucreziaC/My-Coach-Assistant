package com.lucreziacarena.mycoachassistant.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lucreziacarena.mycoachassistant.navigation.NavigationItem
import com.lucreziacarena.mycoachassistant.views.AthletesScreen
import com.lucreziacarena.mycoachassistant.views.LeaderboardScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Athletes.route) {
        composable(NavigationItem.Athletes.route) {
            AthletesScreen()
        }
        composable(NavigationItem.Leaderboard.route) {
            LeaderboardScreen()
        }

    }
}
