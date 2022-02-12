package com.lucreziacarena.mycoachassistant.repository.api

import com.lucreziacarena.mycoachassistant.repository.results.GetAthleticsResult

interface ApiHelper {

    suspend fun getAthelticsList(seed:String, inc: String, gender: String, results:Int): GetAthleticsResult

}
