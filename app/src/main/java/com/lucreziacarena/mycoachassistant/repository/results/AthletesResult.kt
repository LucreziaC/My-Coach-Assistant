package com.lucreziacarena.mycoachassistant.repository.results

import com.example.catchemall.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel


sealed class AthletesResult {
    data class Success(val athleticsList: List<AthleteModel>) : AthletesResult()
    data class Failure(val error: AthletesError) : AthletesResult()
}

sealed class NetworkResponse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : NetworkResponse<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResponse<T>(data, message)
    class Loading<T>(data: T? = null) : NetworkResponse<T>(data)
}
