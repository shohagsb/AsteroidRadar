package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidApiRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel (application: Application): ViewModel() {
    //enum class MarsApiStatus { LOADING, ERROR, DONE }
    private val authToken = "xrPTulymFhQYzSnSOz8XOQhVSUKZxvdMcWikTMxs"

    private val database = getDatabase(application)
    private val repository = AsteroidApiRepository(database)

    private val _status = MutableLiveData<String>()

    val status: LiveData<String>
        get() = _status

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        getPictureOfDay()
        getAsteroidsJson()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                val picResult = AsteroidApi.retrofitService.getPictureOfDay(authToken)
                _pictureOfDay.value = picResult

            } catch (e: Exception) {
                _pictureOfDay.value = PictureOfDay("", "", "")
            }
        }
    }

    private fun getAsteroidsJson() {
        viewModelScope.launch {
            repository.refreshAsteroidsFromNetwork()
//            val week = getNextSevenDaysFormattedDates()
//            try {
//                val asteroidsResult = parseAsteroidsJsonResult(
//                    JSONObject(
//                        AsteroidApi.retrofitService.getAsteroids(
//                            authToken,
//                            week.first(), week.last()
//                        )
//                    )
//                )
//                _status.value = "success ${asteroidsResult.size}"
//
//            } catch (e: Exception) {
//                _status.value = ("Failure ${e.message}")
//            }
        }
    }

    val sts = repository.status
}