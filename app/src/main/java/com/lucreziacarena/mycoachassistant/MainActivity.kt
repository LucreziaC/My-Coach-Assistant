package com.lucreziacarena.mycoachassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.lucreziacarena.mycoachassistant.db.AppDatabase
import com.lucreziacarena.mycoachassistant.ui.components.BottomNavigationBar
import com.lucreziacarena.mycoachassistant.ui.components.Navigation
import com.lucreziacarena.mycoachassistant.ui.theme.MyCoachAssistantTheme
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel by viewModels<ViewModelTest>()

        setContent {
            MyCoachAssistantTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, viewmodel: ViewModelTest) {
    Text(text = "Hello $name!")
    println(viewmodel.state.value.posts)

}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Navigation(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}


