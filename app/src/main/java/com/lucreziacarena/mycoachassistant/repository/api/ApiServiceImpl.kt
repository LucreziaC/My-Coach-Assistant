package com.lucreziacarena.mycoachassistant.repository.api

import com.lucreziacarena.mycoachassistant.repository.models.AthletesDTO
import io.ktor.client.*
import io.ktor.client.request.*

class ApiServiceImpl(private val client: HttpClient): ApiService {
    override suspend fun getAthleticsList(): AthletesDTO {
        return client.get{
            url("${APIUrls.BASE_URL}${APIUrls.ATHLETICS_LIST}")
        }
    }

}
