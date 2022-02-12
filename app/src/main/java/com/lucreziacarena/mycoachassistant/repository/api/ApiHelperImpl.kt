package com.lucreziacarena.mycoachassistant.repository.api

import com.example.catchemall.repository.results.GetAthleticsError.*
import com.lucreziacarena.mycoachassistant.repository.results.GetAthleticsResult
import com.lucreziacarena.mycoachassistant.repository.results.GetAthleticsResult.Failure
import com.lucreziacarena.mycoachassistant.repository.results.GetAthleticsResult.Success
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val service: ApiService) : ApiHelper {

    override suspend fun getAthelticsList(seed: String, inc: String, gender: String, results: Int): GetAthleticsResult {
        return try{
            val athleticsList = service.getAthleticsList(seed, inc, gender, results)
            if(athleticsList.results.isEmpty()){
                Failure(NoAthleticsFound)
            }else
                Success(athleticsList)
        }catch (e: IOException) { // no internet
            Failure(NoInternet)
        } catch (e: SocketTimeoutException) {
            Failure(SlowInternet)
        } catch (e: Exception) {
            e.printStackTrace()
            Failure(ServerError)
        }
    }

}
