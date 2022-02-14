package com.lucreziacarena.mycoachassistant.repository

import com.lucreziacarena.mycoachassistant.repository.api.ApiHelper
import com.lucreziacarena.mycoachassistant.repository.results.AthletesResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Repository {
    suspend fun getAthelticsList(): AthletesResult

}

class RepositoryImpl @Inject constructor(private val apiHelper: ApiHelper) : Repository{
    override suspend fun getAthelticsList(): AthletesResult {
        return apiHelper.getAthelticsList()
    }
}
