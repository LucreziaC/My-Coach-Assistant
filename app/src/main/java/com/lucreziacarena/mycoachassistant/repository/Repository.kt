package com.lucreziacarena.mycoachassistant.repository

import com.lucreziacarena.mycoachassistant.repository.api.ApiHelper
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.models.toDomain
import com.lucreziacarena.mycoachassistant.repository.results.AthletesResult
import com.lucreziacarena.mycoachassistant.repository.results.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface Repository {
    suspend fun getAthelticsList(): Flow<NetworkResponse<List<AthleteModel>>>

}

class RepositoryImpl @Inject constructor(private val apiHelper: ApiHelper) : Repository{
    override suspend fun getAthelticsList(): Flow<NetworkResponse<List<AthleteModel>>> {
        return flow {
            try {
                emit(NetworkResponse.Loading())
                val list = apiHelper.getAthelticsList().results.map { it.toDomain() }
                emit(NetworkResponse.Success(data = list))
            }catch(e:Exception){
                emit(NetworkResponse.Error(e.message ?: "Error"))
            }
        }.flowOn(Dispatchers.IO)
    }
}
