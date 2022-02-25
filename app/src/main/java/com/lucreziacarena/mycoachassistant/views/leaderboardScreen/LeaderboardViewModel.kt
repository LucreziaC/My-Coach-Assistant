package com.lucreziacarena.mycoachassistant.views.leaderboardScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucreziacarena.mycoachassistant.repository.Repository
import com.lucreziacarena.mycoachassistant.repository.models.AthleteStatsModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.repository.results.DataState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    val state: MutableState<LeaderboardStates> = mutableStateOf(LeaderboardStates.Empty)
    val action: MutableState<AthleteScreenAction> =
        mutableStateOf(AthleteScreenAction.NoAction)

    fun send(event: AthleteScreenEvent) {
        when(event){
            is AthleteScreenEvent.InsertedMeters -> {
                action.value = AthleteScreenAction.NavigateToSessionScreen(event.meters)
            }
            AthleteScreenEvent.Init -> {
                action.value = AthleteScreenAction.NoAction
            }
        }
    }


    init {
        getAthleteByPeakSpeed()
    }

    private fun getAthleteByPeakSpeed() {
        viewModelScope.launch {
            repository.getAthletStatsByPeakSpeed().collect { flowResult ->
                when (flowResult) {
                    is DataState.Error -> state.value = LeaderboardStates.Error(flowResult.error)
                    is DataState.Loading -> {
                        state.value = LeaderboardStates.Loading
                    }
                    is DataState.Success -> {
                        state.value = LeaderboardStates.Content(flowResult.data)
                    }
                }
            }
        }
    }
private fun getAthleteByNumLap() {
        viewModelScope.launch {
            repository.getAthletStatsByNumLap().collect { flowResult ->
                when (flowResult) {
                    is DataState.Error -> state.value = LeaderboardStates.Error(flowResult.error)
                    is DataState.Loading -> {
                        state.value = LeaderboardStates.Loading
                    }
                    is DataState.Success -> {
                        state.value = LeaderboardStates.Content(flowResult.data)
                    }
                }
            }
        }
    }


}

sealed class LeaderboardStates {
    data class Content(val athletesList: List<AthleteStatsModel>) : LeaderboardStates()
    object Loading: LeaderboardStates()
    data class Error(val error: AthletesError): LeaderboardStates()
    object Empty: LeaderboardStates()
}

sealed class AthleteScreenEvent{
    data class InsertedMeters(val meters: Int) : AthleteScreenEvent()
    object Init : AthleteScreenEvent()
}


sealed class AthleteScreenAction{
    data class NavigateToSessionScreen(val meters: Int) : AthleteScreenAction()
    object NoAction : AthleteScreenAction()
}

