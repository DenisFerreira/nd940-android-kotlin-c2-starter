package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = NasaRepository(database)
    val apod: LiveData<PictureOfDay?> = repository.pictureOfDay
    val listAsteroid : LiveData<List<Asteroid>> = repository.asteroids

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
//            The error was found, the refresh function is making the app crash
            repository.refreshPictureOfDay()
            repository.refreshAsteroids()
        }
    }


}