package com.udacity.asteroidradar.repository

import android.util.Log
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val authToken = "xrPTulymFhQYzSnSOz8XOQhVSUKZxvdMcWikTMxs"

class AsteroidApiRepository(private val database: AsteroidsDatabase) {

    private var _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    val asteroids: LiveData<List<Asteroid>> = Transformations
        .map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val picResult = AsteroidApi.retrofitService.getPictureOfDay(authToken)


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
                Log.d("RepoTAG", "getAsteroidsJson: success ${asteroidsResult.size}")

            } catch (e: Exception) {
                Log.d("RepoTAG", "getAsteroidsJson: Failure ${e.message}")
            }
        }
    }
}