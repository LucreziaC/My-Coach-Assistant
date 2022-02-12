package com.lucreziacarena.mycoachassistant.repository.results

import com.example.catchemall.repository.results.GetAthleticsError
import com.lucreziacarena.mycoachassistant.repository.models.AthleticsDTO


sealed class GetAthleticsResult {
    data class Success(val athleticsList: AthleticsDTO) : GetAthleticsResult()
    data class Failure(val error: GetAthleticsError) : GetAthleticsResult()
}
