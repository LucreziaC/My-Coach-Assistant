package com.lucreziacarena.mycoachassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucreziacarena.mycoachassistant.ui.theme.MyCoachAssistantTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel by viewModels<ViewModelTest>()
        setContent {
            MyCoachAssistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android", viewmodel)


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, viewmodel: ViewModelTest) {
    Text(text = "Hello $name!")
    println(viewmodel.state.value.posts)

}


