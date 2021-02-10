package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = NasaRepository(database)
    val apod: LiveData<PictureOfDay?> = repository.pictureOfDay


    private val apiFilter = MutableLiveData<NasaApiFilter>(NasaApiFilter.SHOW_WEEK)
    fun updateFilter(filter: NasaApiFilter) {
        apiFilter.value = filter
    }

    val asteroidsList = Transformations.switchMap(apiFilter) {
        when (it) {
            NasaApiFilter.SHOW_ALL -> repository.allAsteroids
            NasaApiFilter.SHOW_TODAY -> repository.todayAsteroid
            else -> repository.weekAsteroids
        }

    }
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    fun navigateDetailClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun doneNavigating() {
        _navigateToAsteroidDetail.value = null
    }


    init {
        viewModelScope.launch {
            repository.refreshPictureOfDay()
            repository.refreshAsteroids()
        }
    }

        enum class NasaApiFilter {
            SHOW_ALL, SHOW_WEEK, SHOW_TODAY
        }

}