package com.lucreziacarena.mycoachassistant.repository.models
import com.lucreziacarena.mycoachassistant.sql.Athlete
import kotlinx.serialization.Serializable

@Serializable
data class AthletesDTO(
    val results: List<Result>
) {
    @Serializable
    data class Result(
        val name: Name,
        val picture: Picture
    ) {
        @Serializable
        data class Name(
            val first: String,
            val last: String,
            val title: String
        )

        @Serializable
        data class Picture(
            val large: String,
            val medium: String,
            val thumbnail: String
        )

    }
}

fun AthletesDTO.Result.toDomain(): Athlete {
    return Athlete(this.name.first, this.name.last, this.picture.medium)
}

