package com.example.catchemall.repository.results

sealed class GetAthleticsError {
    object NoAthleticsFound : GetAthleticsError()
    object NoInternet : GetAthleticsError()
    object SlowInternet : GetAthleticsError()
    object ServerError : GetAthleticsError()
}
