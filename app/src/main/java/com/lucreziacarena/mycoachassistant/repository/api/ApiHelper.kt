package com.lucreziacarena.mycoachassistant.repository.api

import com.lucreziacarena.mycoachassistant.repository.results.AthletesResult
import kotlinx.coroutines.flow.Flow

interface ApiHelper {

    suspend fun getAthelticsList(): AthletesResult

}
