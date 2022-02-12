package com.lucreziacarena.mycoachassistant.repository.api

import com.lucreziacarena.mycoachassistant.repository.models.AthleticsDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(APIUrls.ATHLETICS_LIST)
    suspend fun getAthleticsList(
        @Path("seed") seed: String,
        @Path("inc") inc: String,
        @Path("gender") gender: String,
        @Path("results") resultsNumber: Int,
    ): AthleticsDTO


}
