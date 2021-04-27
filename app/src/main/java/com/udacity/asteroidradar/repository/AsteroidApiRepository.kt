package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val authToken = "xrPTulymFhQYzSnSOz8XOQhVSUKZxvdMcWikTMxs"

class AsteroidApiRepository {

    private var _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val picResult = AsteroidApi.retrofitService.getPictureOfDay(authToken)


            } catch (e: Exception) {

            }
        }
    }

    suspend fun getAsteroidsJson() {
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
                Log.d("RepoTAG", "getAsteroidsJson: success ${asteroidsResult.size}")

            } catch (e: Exception) {
                Log.d("RepoTAG", "getAsteroidsJson: Failure ${e.message}")
            }
        }
    }
}