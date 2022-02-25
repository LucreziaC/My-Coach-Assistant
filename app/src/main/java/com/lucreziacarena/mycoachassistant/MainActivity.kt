package com.lucreziacarena.mycoachassistant

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lucreziacarena.mycoachassistant.ui.theme.MyCoachAssistantTheme
import com.lucreziacarena.mycoachassistant.ui.components.BottomNavigationBar
import com.lucreziacarena.mycoachassistant.ui.components.Navigation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCoachAssistantTheme {
                MainScreen()
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController,bottomBarState) }

    ) { innerPadding -> //gives the right amount of padding for the element in scaffold
        Box(modifier = Modifier.padding(innerPadding)){
            Navigation(navController,bottomBarState)
        }
    }
}

@Composable
public fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(navController.currentBackStackEntry?.destination?.route)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}


