package com.lucreziacarena.mycoachassistant.repository.results

import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel


sealed class AthletesResult {
    data class Success(val athleticsList: List<AthleteModel>) : AthletesResult()
    data class Failure(val error: AthletesError) : AthletesResult()
}

sealed class DataState<out T>{
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val error: AthletesError) : DataState<Nothing>()
    object Loading: DataState<Nothing>()
}
