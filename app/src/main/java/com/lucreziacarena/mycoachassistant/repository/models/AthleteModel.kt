package com.lucreziacarena.mycoachassistant.repository.models


data class AthleteModel(
    val name: Name,
    val picture: Picture
) {
    data class Name(
        val first: String,
        val last: String,
        val title: String
    )
    data class Picture(
        val large: String,
        val medium: String,
        val thumbnail: String
    )
}
