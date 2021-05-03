package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidApiRepository
import kotlinx.coroutines.launch

enum class AsteroidFilter { WEEK, TODAY, SAVED }
class MainViewModel(application: Application) : ViewModel() {
    private val database = getDatabase(application)
    private val repository = AsteroidApiRepository(database)

    var asteroids: LiveData<List<Asteroid>>

    init {
        getPictureOfDay()
        getAsteroidsJson()
        asteroids = repository.weeksAsteroids
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


    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: MutableLiveData<Asteroid?>
        get() = _navigateToDetailFragment

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToDetailFragment.value = null
    }

    fun updateFilter(filter: AsteroidFilter) {
        if (filter == AsteroidFilter.TODAY) {
            asteroids = repository.todayAsteroids
            Log.d("MainFragmentTag", "onCreateView: ${repository.todayAsteroids.value?.size}")

        }
//        asteroids = when (filter) {
//            AsteroidFilter.WEEK -> repository.weeksAsteroids
//            AsteroidFilter.TODAY -> repository.todayAsteroids
//            else -> repository.savedAsteroids
//        }


    }

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

