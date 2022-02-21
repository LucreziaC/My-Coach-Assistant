package com.lucreziacarena.mycoachassistant.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.lucreziacarena.mycoachassistant.navigation.NavigationItem
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.views.LeaderboardScreen
import com.lucreziacarena.mycoachassistant.views.athletesScreen.AthletesScreen
import com.lucreziacarena.mycoachassistant.views.sessionScreen.SessionScreen

@ExperimentalMaterial3Api
@Composable
fun Navigation(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    NavHost(navController, startDestination = NavigationItem.Athletes.route) {
        composable(NavigationItem.Athletes.route) {
            bottomBarState.value = true
            AthletesScreen(navController)
        }
        composable(NavigationItem.Leaderboard.route) {
            bottomBarState.value = true
            LeaderboardScreen()
        }
        composable(
            route = NavigationItem.Session.route + "?athlete={athlete}",
            arguments = listOf(
                navArgument("athlete") {
                    type = NavType.StringType
                    nullable = true
                })
        ){backStackEntry ->
                val athleteJson = backStackEntry.arguments?.getString("athlete")
                val athlete = Gson().fromJson(athleteJson, AthleteModel::class.java)
                bottomBarState.value = false
                SessionScreen(navController = navController,athlete)
            }

    }
}
