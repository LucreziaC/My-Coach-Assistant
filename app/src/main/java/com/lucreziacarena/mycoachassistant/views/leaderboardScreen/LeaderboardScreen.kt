package com.lucreziacarena.mycoachassistant.views.leaderboardScreen

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucreziacarena.mycoachassistant.repository.models.AthleteStatsModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.ui.components.TopAppBar


@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(navController: NavController) {

    val viewModel = hiltViewModel<LeaderboardViewModel>()

    val athletesList = remember { mutableListOf<AthleteStatsModel>() }
    val errorMessage = remember { mutableStateOf("") }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showLoading = remember { mutableStateOf(false) }
    val showNumLapSorted = remember { mutableStateOf(false) }
    val showPeakSpeedSorted = remember { mutableStateOf(false) }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    observeStates(viewModel.state.value, athletesList, errorMessage, showErrorDialog, showLoading)


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                "Leaderboard",
                navController = navController,
                scrollBehavior = scrollBehavior,
                backNavigation = false
            )
        }
    ) {
        val isCheckedPeak = remember { mutableStateOf(false) }
        val isCheckedLaps = remember { mutableStateOf(false) }
        LazyColumn() {
            stickyHeader {
                    Column(modifier = Modifier.padding(start = 10.dp)){
                        Text("Sort by: ", fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text("Peak speed")
                            Checkbox(checked = isCheckedPeak.value, onCheckedChange = {
                                isCheckedPeak.value = it
                                isCheckedLaps.value = false
                                showNumLapSorted.value= false
                                showPeakSpeedSorted.value= true
                                viewModel.send(LeaderboardEvents.GetPeakSortedList)
                            })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text("Laps number")
                            Checkbox(checked = isCheckedLaps.value, onCheckedChange = {
                                isCheckedLaps.value = it
                                isCheckedPeak.value = false
                                showNumLapSorted.value= true
                                showPeakSpeedSorted.value= false
                                viewModel.send(LeaderboardEvents.GetLapsSortedList)
                            })
                        }
                    }
            }
            itemsIndexed(athletesList) { index, athlete ->
                AthleteStatsRowItem(athlet = athlete, peakSpeed = showPeakSpeedSorted.value, numLap = showNumLapSorted.value)
                if (index < athletesList.lastIndex)
                    Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
            }
        }
    }
}


fun observeStates(
    state: LeaderboardStates,
    athletesList: MutableList<AthleteStatsModel>,
    errorMessage: MutableState<String>,
    showErrorDialog: MutableState<Boolean>,
    showLoading: MutableState<Boolean>
) {
    when (state) {
        is LeaderboardStates.Content -> {
            showLoading.value = false
            athletesList.clear()
            athletesList.addAll(state.athletesList)

        }
        LeaderboardStates.Empty -> {
            showLoading.value = false
        }
        is LeaderboardStates.Error -> {
            showLoading.value = false
            when (state.error) {
                is AthletesError.GenericError -> {
                    errorMessage.value = state.error.message ?: "Errore"
                    showErrorDialog.value = true
                }
                AthletesError.NoAthletesFound -> {}
            }
        }
        LeaderboardStates.Loading -> showLoading.value = true
    }
}
