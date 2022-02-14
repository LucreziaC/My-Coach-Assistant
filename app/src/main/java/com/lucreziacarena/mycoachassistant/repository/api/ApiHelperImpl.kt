package com.lucreziacarena.mycoachassistant.repository.api

import com.example.catchemall.repository.results.AthletesError.*
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.models.toDomain
import com.lucreziacarena.mycoachassistant.repository.results.AthletesResult
import com.lucreziacarena.mycoachassistant.repository.results.AthletesResult.Failure
import com.lucreziacarena.mycoachassistant.repository.results.AthletesResult.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val service: ApiService) : ApiHelper {

    override suspend fun getAthelticsList(): AthletesResult {
        try{
            val athleticsList: List<AthleteModel> = service.getAthleticsList().results.map{ it.toDomain()}
            if(athleticsList.isEmpty()){
                //emit(Failure(NoAthleticsFound))
            }else
                //emit(Success(athleticsList))
                return Success(athleticsList)
        }catch (e: IOException) { // no internet
           // emit(Failure(NoInternet))
        } catch (e: SocketTimeoutException) {
           // emit(Failure(SlowInternet))
        } catch (e: Exception) {
            e.printStackTrace()
           // emit(Failure(ServerError))
        }
        return Failure(NoAthleticsFound)
    }

}
