package com.lucreziacarena.mycoachassistant.views.athletesScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucreziacarena.mycoachassistant.repository.Repository
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.repository.results.DataState
import com.lucreziacarena.mycoachassistant.views.athletesScreen.States.Content
import com.lucreziacarena.mycoachassistant.views.athletesScreen.States.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AthletesViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    val state: MutableState<States> = mutableStateOf(States.Empty)
    val action: MutableState<AthleteScreenAction> =
        mutableStateOf(AthleteScreenAction.NoAction)

    val athletList = mutableListOf<AthleteModel>()

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
        viewModelScope.launch {
            repository.getAthelticsList().collect(){flowResult ->
                when(flowResult){
                    is DataState.Error -> state.value = States.Error(flowResult.error)
                    is DataState.Loading -> {
                        state.value = Loading
                    }
                    is DataState.Success -> {
                        state.value = Content(flowResult.data)
                        athletList.addAll(flowResult.data)
                    }
                }
            }
        }
    }

}

sealed class States {
    data class Content(val athletesList: List<AthleteModel>) : States()
    object Loading: States()
    data class Error(val error: AthletesError): States()
    object Empty: States()
}

sealed class AthleteScreenEvent{
    data class InsertedMeters(val meters: Int) : AthleteScreenEvent()
    object Init : AthleteScreenEvent()
}


sealed class AthleteScreenAction{
    data class NavigateToSessionScreen(val meters: Int) : AthleteScreenAction()
    object NoAction : AthleteScreenAction()
}

