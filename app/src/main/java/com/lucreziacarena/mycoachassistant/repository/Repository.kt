package com.lucreziacarena.mycoachassistant.repository

import com.lucreziacarena.mycoachassistant.db.AppDatabase
import com.lucreziacarena.mycoachassistant.repository.api.ApiHelper
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.models.AthleteStatsModel
import com.lucreziacarena.mycoachassistant.repository.models.toDbEntity
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
    suspend fun getAthelticsList(): Flow<DataState<List<AthleteModel>>>
    suspend fun saveAthletStats(athlete: AthleteModel, numLap: Int, speedMax: Long): Flow<DataState<Boolean>>
    suspend fun getAthletStatsByNumLap(): Flow<DataState<List<AthleteStatsModel>>>
    suspend fun getAthletStatsByPeakSpeed(): Flow<DataState<List<AthleteStatsModel>>>
}

class RepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val sharedPreferences: PreferencesHelper,
    private val database: AppDatabase
) : Repository {

    companion object {
        private const val ATHLETES_CACHE_TIME: Long = 1000 * 60 * 60 * 12 //6 hours
    }

    override suspend fun getAthelticsList(): Flow<DataState<List<AthleteModel>>> {
        return flow {
            try {
                emit(DataState.Loading)
                val now = Calendar.getInstance().timeInMillis
                val lastTimeChecked = sharedPreferences.getLongPreference(PreferencesHelper.LAST_NETWORK_LOOKUP)
                if (now > lastTimeChecked + ATHLETES_CACHE_TIME) { //if is more than 6hours since last time I get athletes from web service
                    database.athleteQueries.clearTable() //the athlets list has random items, so I have to clear table in order to fetch new athletes from ws
                    val athleteslList = apiHelper.getAthelticsList().results.map { it.toDomain() }
                    athleteslList.map {athleteModel ->
                        database.athleteQueries.insert(athleteModel.toDbEntity())
                    }
                }
                val athletesFromDB = database.athleteQueries.selectAll().executeAsList().map{
                    it.toDomain()
                }
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

    override suspend fun saveAthletStats(athlete: AthleteModel, numLap: Int, speedMax: Long): Flow<DataState<Boolean>> {
        return flow {
            try {
                emit(DataState.Loading)
                database.athleteQueries.insertAthletSession(athlete.name, athlete.surname, athlete.picture, speedMax, numLap.toLong())
                emit(DataState.Success(data = true))
            } catch (e: Exception) {
                emit(DataState.Error(AthletesError.GenericError(e.message ?: "Error")))
            }
        }
    }

    override suspend fun getAthletStatsByNumLap(): Flow<DataState<List<AthleteStatsModel>>> {
        return flow {
            try {
                emit(DataState.Loading)
                val data = database.athleteQueries.getAthletesOrderByNumLap().executeAsList()
                lateinit var athleteList: List<AthleteStatsModel>
                if(data.isNotEmpty()) {
                    athleteList = data.map {
                        AthleteStatsModel(it.name!!, it.surname!!, it.picture!!, it.peakSpeed!!, it.numLap!!.toInt())
                    }
                }else{
                    emit(DataState.Error(AthletesError.NoAthletesFound))
                }
                emit(DataState.Success(data = athleteList))
            } catch (e: Exception) {
                emit(DataState.Error(AthletesError.GenericError(e.message ?: "Error")))
            }
        }
    }

    override suspend fun getAthletStatsByPeakSpeed(): Flow<DataState<List<AthleteStatsModel>>> {
        return flow {
            try {
                emit(DataState.Loading)
                val data = database.athleteQueries.getAthletesOrderByPeakSpeed().executeAsList()
                lateinit var athleteList: List<AthleteStatsModel>
                if(data.isNotEmpty()) {
                    athleteList = data.map {
                        AthleteStatsModel(it.name!!, it.surname!!, it.picture!!, it.peakSpeed!!, it.numLap!!.toInt())
                    }
                }else{
                    emit(DataState.Error(AthletesError.NoAthletesFound))
                }
                emit(DataState.Success(data = athleteList))
            } catch (e: Exception) {
                emit(DataState.Error(AthletesError.GenericError(e.message ?: "Error")))
            }
        }
    }
}

private fun Athlete.toDomain(): AthleteModel {
    return AthleteModel(this.name?:"", this.surname?:"", this.picture?:"")
}


