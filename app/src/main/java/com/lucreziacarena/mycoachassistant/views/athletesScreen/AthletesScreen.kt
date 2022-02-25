package com.lucreziacarena.mycoachassistant.views.athletesScreen

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.lucreziacarena.mycoachassistant.R
import com.lucreziacarena.mycoachassistant.navigation.NavigationItem
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.ui.components.TopAppBar
import com.lucreziacarena.mycoachassistant.views.leaderboardScreen.AthleteScreenAction
import com.lucreziacarena.mycoachassistant.views.leaderboardScreen.AthleteScreenEvent


@ExperimentalMaterial3Api
@Composable
fun AthletesScreen(navController: NavController) {
    val viewModel = hiltViewModel<AthletesViewModel>()
    val athletesList = remember { mutableListOf<AthleteModel>() }
    val errorMessage = remember { mutableStateOf("") }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showLoading = remember { mutableStateOf(false) }
    val meters = remember { mutableStateOf(0) }
    val athleteChosen: MutableState<AthleteModel?> = remember { mutableStateOf(null) }
    observeStates(viewModel.state.value, athletesList, errorMessage, showErrorDialog, showLoading)
    observeAction(viewModel,viewModel.action.value, athleteChosen,navController)

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar("Athlets List", navController = navController, scrollBehavior = scrollBehavior, backNavigation = false) }
    ) {

        val openDialog = remember { mutableStateOf(false) }
        var text by remember { mutableStateOf("") }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = stringResource(R.string.set_distance_alert_title))
                },
                text = {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        label = {
                            Text("meters")
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                            meters.value = text.toInt()
                            viewModel.send(AthleteScreenEvent.InsertedMeters(meters.value))
                        }
                    ) {
                        Surface {
                            Text(stringResource(R.string.ok))
                        }
                    }
                }
            )
        }

        LazyColumn(
        ) {
            itemsIndexed(athletesList) { index, athlete ->
                AthleteRowItem(athlete) {
                    openDialog.value = true
                    athleteChosen.value = athlete
                }

                if (index < athletesList.lastIndex)
                    Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
            }
        }
    }

}

fun observeAction(viewModel: AthletesViewModel, action: AthleteScreenAction, athleteChosen: MutableState<AthleteModel?>, navController: NavController) {
    when(action){
        is AthleteScreenAction.NavigateToSessionScreen -> {
            val athlete = Gson().toJson(athleteChosen.value)
            navController.navigate(NavigationItem.Session.route + "?athlete=$athlete&meters=${action.meters}")
            viewModel.send(AthleteScreenEvent.Init)
        }
        AthleteScreenAction.NoAction -> {}
    }
}

fun observeStates(
    state: States,
    athletesList: MutableList<AthleteModel>,
    errorMessage: MutableState<String>,
    showErrorDialog: MutableState<Boolean>,
    showLoading: MutableState<Boolean>
) {
    when (state) {
        is States.Content -> {
            showLoading.value = false
            athletesList.clear()
            athletesList.addAll(state.athletesList)

        }
        States.Empty -> {
            showLoading.value = false
        }
        is States.Error -> {
            showLoading.value = false
            when (state.error) {
                is AthletesError.GenericError -> {
                    errorMessage.value = state.error.message ?: "Errore"
                    showErrorDialog.value = true
                }
                AthletesError.NoAthletesFound -> {

                }
            }
        }
        States.Loading -> showLoading.value = true
    }
}
