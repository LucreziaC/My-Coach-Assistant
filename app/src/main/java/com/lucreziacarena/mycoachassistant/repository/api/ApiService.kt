package com.lucreziacarena.mycoachassistant.repository.api

import com.lucreziacarena.mycoachassistant.repository.models.AthletesDTO

interface ApiService {
    suspend fun getAthleticsList(): AthletesDTO
}
