package com.lucreziacarena.mycoachassistant.repository.results

import com.example.catchemall.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel


sealed class AthletesResult {
    data class Success(val athleticsList: List<AthleteModel>) : AthletesResult()
    data class Failure(val error: AthletesError) : AthletesResult()
}
