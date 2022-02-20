package com.lucreziacarena.mycoachassistant.repository.results

sealed class AthletesError {
    object NoAthletesFound : AthletesError()
    data class GenericError(val message: String?): AthletesError()
}
