package com.lucreziacarena.mycoachassistant.repository.api

import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val service: ApiService) : ApiHelper {

    override suspend fun getAthelticsList() = service.getAthleticsList()


}
