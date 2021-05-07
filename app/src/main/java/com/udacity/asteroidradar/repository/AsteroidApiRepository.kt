package com.udacity.asteroidradar.repository


import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val authToken = "xrPTulymFhQYzSnSOz8XOQhVSUKZxvdMcWikTMxs"

class AsteroidApiRepository(private val database: AsteroidsDatabase) {
    private val week = getNextSevenDaysFormattedDates()

    val weeksAsteroids: Flow<List<Asteroid>>
        get() = database.asteroidDao.getWeeksAsteroids(week.first())

    val todayAsteroids: Flow<List<Asteroid>>
        get() = database.asteroidDao.getTodayAsteroids(week.first())

    val savedAsteroids: Flow<List<Asteroid>>
        get() = database.asteroidDao.getSavedAsteroids()

    val getPictureOfDay: Flow<PictureOfDay> = flow {
        val pictureOfDay = AsteroidApi.retrofitService.getPictureOfDay(authToken)
        emit(pictureOfDay)
    }.flowOn(Dispatchers.IO)


    suspend fun refreshAsteroidsFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsResult = parseAsteroidsJsonResult(
                    JSONObject(
                        AsteroidApi.retrofitService.getAsteroids(
                            authToken,
                            week.first(), week.last()
                        )
                    )
                )
                database.asteroidDao.insertAll(*asteroidsResult.toTypedArray())
            } catch (e: Exception) {

            }
        }
    }
}