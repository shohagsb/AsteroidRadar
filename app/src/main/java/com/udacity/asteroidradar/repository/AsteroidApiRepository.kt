package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val authToken = "xrPTulymFhQYzSnSOz8XOQhVSUKZxvdMcWikTMxs"

class AsteroidApiRepository(private val database: AsteroidsDatabase) {

    private var _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids()

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.Main) {
            try {
                val picResult = AsteroidApi.retrofitService.getPictureOfDay(authToken)
                _pictureOfDay.value = picResult
            } catch (e: Exception) {

            }
        }
    }

    suspend fun refreshAsteroidsFromNetwork() {
        withContext(Dispatchers.IO) {
            val week = getNextSevenDaysFormattedDates()
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