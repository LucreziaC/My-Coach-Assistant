package com.lucreziacarena.mycoachassistant.views.sessionScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucreziacarena.mycoachassistant.repository.Repository
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.repository.results.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionScreenViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    val state: MutableState<SessionState> = mutableStateOf(SessionState.Empty)


    fun send(event: SessionScreenEvent){
        when(event){
            is SessionScreenEvent.OnCloseSession -> {
                saveSessionInDB(event.athlete, event.numLap, event.speedMax)
            }
        }
    }

    private fun saveSessionInDB(athlete: AthleteModel, numLap: Int, speedMax: Long) {
        viewModelScope.launch {
            repository.saveAthletStats(athlete, numLap, speedMax).collect(){flowResult ->
                when(flowResult){
                    is DataState.Loading -> {
                        state.value = SessionState.Loading
                    }
                    is DataState.Success -> {
                        state.value = SessionState.Content(flowResult.data)
                    }
                    else -> {}
                }
            }
        }
    }

}

sealed class SessionScreenEvent {
    data class OnCloseSession(val athlete: AthleteModel, val numLap: Int, val speedMax: Long) : SessionScreenEvent()
}
sealed class SessionState {
    data class Content(val inserted: Boolean) : SessionState()
    object Loading: SessionState()
    data class Error(val error: AthletesError): SessionState()
    object Empty: SessionState()
}
