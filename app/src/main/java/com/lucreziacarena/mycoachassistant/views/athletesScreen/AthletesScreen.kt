package com.lucreziacarena.mycoachassistant.views.athletesScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError


@Composable
fun AthletesScreen() {
    val viewModel = hiltViewModel<AthletesViewModel>()
    val athletesList = remember{ mutableListOf<AthleteModel>() }
    val errorMessage = remember { mutableStateOf("") }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showLoading = remember { mutableStateOf(false) }
    observeStates(viewModel.state.value, athletesList, errorMessage, showErrorDialog, showLoading)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(athletesList) { index, athlete ->
            AthletRowItem(athlete)

            if (index < athletesList.lastIndex)
                Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
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
    when(state){
        is States.Content -> {
            showLoading.value = false
            athletesList.addAll(state.athletesList)
        }
        States.Empty -> {
            showLoading.value = false
        }
        is States.Error -> {
            showLoading.value = false
            when(state.error){
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
