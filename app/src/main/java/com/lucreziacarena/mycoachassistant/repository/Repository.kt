package com.lucreziacarena.mycoachassistant.repository

import com.lucreziacarena.mycoachassistant.db.AppDatabase
import com.lucreziacarena.mycoachassistant.repository.api.ApiHelper
import com.lucreziacarena.mycoachassistant.repository.models.toDomain
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.repository.results.DataState
import com.lucreziacarena.mycoachassistant.sql.Athlete
import com.lucreziacarena.mycoachassistant.utils.PreferencesHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

interface Repository {
    suspend fun getAthelticsList(): Flow<DataState<List<Athlete>>>

}

class RepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val sharedPreferences: PreferencesHelper,
    private val database: AppDatabase
) : Repository {

    companion object {
        private const val ATHLETES_CACHE_TIME: Long = 1000 * 60 * 60 * 12 //6 hours
    }

    override suspend fun getAthelticsList(): Flow<DataState<List<Athlete>>> {
        return flow {
            try {
                emit(DataState.Loading)
                val now = Calendar.getInstance().timeInMillis
                val lastTimeChecked = sharedPreferences.getLongPreference(PreferencesHelper.LAST_NETWORK_LOOKUP)
                if (now > lastTimeChecked + ATHLETES_CACHE_TIME) { //if is more than 6hours since last time I get athletes from web service
                    val athleteslList = apiHelper.getAthelticsList().results.map { it.toDomain() }
                    athleteslList.map {athlete ->
                        database.athleteQueries.insert(athlete)
                    }
                }
                val athletesFromDB = database.athleteQueries.selectAll().executeAsList()
                if(athletesFromDB.isNullOrEmpty()){
                    emit(DataState.Error(AthletesError.NoAthletesFound))
                }
                else {
                    emit(DataState.Success(data = athletesFromDB))
                }
               sharedPreferences.setLongPreference(PreferencesHelper.LAST_NETWORK_LOOKUP, now)
            } catch (e: Exception) {
                emit(DataState.Error(AthletesError.GenericError(e.message ?: "Error")))
            }
        }
    }
}
