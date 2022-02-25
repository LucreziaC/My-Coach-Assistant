package com.lucreziacarena.mycoachassistant.repository.models

import com.lucreziacarena.mycoachassistant.sql.Athlete


data class AthleteModel(
    val name: String,
    val surname: String,
    val picture: String
)

fun AthleteModel.toDbEntity(): Athlete {
    return Athlete(name  = this.name, surname = this.surname, picture = this.picture)
}

data class AthleteStatsModel(
    val name: String,
    val surname: String,
    val picture: String,
    val peakSpeed: Long,
    val numLap:Int
)