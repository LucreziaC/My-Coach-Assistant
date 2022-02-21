package com.lucreziacarena.mycoachassistant.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lucreziacarena.mycoachassistant.navigation.NavigationItem
import com.lucreziacarena.mycoachassistant.views.athletesScreen.AthletesScreen
import com.lucreziacarena.mycoachassistant.views.LeaderboardScreen

@ExperimentalMaterial3Api
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Athletes.route) {
        composable(NavigationItem.Athletes.route) {
            AthletesScreen(navController)
        }
        composable(NavigationItem.Leaderboard.route) {
            LeaderboardScreen()
        }

    }
}
