package com.lucreziacarena.mycoachassistant.repository

import com.lucreziacarena.mycoachassistant.repository.api.ApiHelper
import com.lucreziacarena.mycoachassistant.repository.results.GetAthleticsResult
import javax.inject.Inject

interface Repository {
    suspend fun getAthelticsList(seed: String, inc: String, gender: String, results:Int): GetAthleticsResult

}

class RepositoryImpl @Inject constructor(private val apiHelper: ApiHelper) : Repository{
    override suspend fun getAthelticsList(seed: String, inc: String, gender: String, results: Int): GetAthleticsResult = apiHelper.getAthelticsList(seed, inc, gender,results )
}
