package com.example.catchemall.repository.results

sealed class AthletesError {
    object NoAthleticsFound : AthletesError()
    object NoInternet : AthletesError()
    object SlowInternet : AthletesError()
    object ServerError : AthletesError()
}
