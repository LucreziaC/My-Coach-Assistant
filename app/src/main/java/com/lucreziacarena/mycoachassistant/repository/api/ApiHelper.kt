package com.lucreziacarena.mycoachassistant.repository.api

import com.lucreziacarena.mycoachassistant.repository.models.AthletesDTO


interface ApiHelper {

    suspend fun getAthelticsList(): AthletesDTO

}
