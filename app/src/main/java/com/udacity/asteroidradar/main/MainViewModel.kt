package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidApiRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : ViewModel() {
    //enum class MarsApiStatus { LOADING, ERROR, DONE }
    //private val authToken = "xrPTulymFhQYzSnSOz8XOQhVSUKZxvdMcWikTMxs"

    private val database = getDatabase(application)
    private val repository = AsteroidApiRepository(database)

    init {
        getPictureOfDay()
        getAsteroidsJson()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            repository.getPictureOfDay()
        }
    }
    val picOfDay = repository.pictureOfDay

    private fun getAsteroidsJson() {
        viewModelScope.launch {
            repository.refreshAsteroidsFromNetwork()
        }
    }
    val asteroids = repository.asteroids

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

