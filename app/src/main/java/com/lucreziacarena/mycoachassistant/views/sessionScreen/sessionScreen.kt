package com.lucreziacarena.mycoachassistant.views.sessionScreen

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.ui.components.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(navController: NavController, athlete: AthleteModel) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                "${athlete.name} ${athlete.surname}'s session",
                navController = navController,
                scrollBehavior = scrollBehavior,
                backNavigation = true,
                picture = athlete.picture
            )
        }
    ) {

    }
}
