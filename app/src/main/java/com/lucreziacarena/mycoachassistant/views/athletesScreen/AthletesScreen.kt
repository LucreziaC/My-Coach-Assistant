package com.lucreziacarena.mycoachassistant.views.athletesScreen

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.ui.components.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.lucreziacarena.mycoachassistant.R


@ExperimentalMaterial3Api
@Composable
fun AthletesScreen(navController: NavController) {
    val viewModel = hiltViewModel<AthletesViewModel>()
    val athletesList = remember { mutableListOf<AthleteModel>() }
    val errorMessage = remember { mutableStateOf("") }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showLoading = remember { mutableStateOf(false) }
    observeStates(viewModel.state.value, athletesList, errorMessage, showErrorDialog, showLoading)

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar("Athlets List", navController = navController, scrollBehavior = scrollBehavior) }
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
                }

                if (index < athletesList.lastIndex)
                    Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
            }
        }
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
