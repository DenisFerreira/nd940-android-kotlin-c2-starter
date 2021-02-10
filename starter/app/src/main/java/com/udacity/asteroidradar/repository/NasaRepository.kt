package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseStringToAsteroidList
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class NasaRepository(private val database: NasaDatabase) {
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = database.pictureofDayDAO.getPictureOfDay().map { it?.asModel() }

    val weekAsteroids: LiveData<List<Asteroid>>
        get() {
            val calendar = Calendar.getInstance()
            val currentTime = calendar.time
            val dateFormat =
                SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val nextTime = calendar.time
            return database.asteroidDAO.getAll(
                dateFormat.format(currentTime),
                dateFormat.format(nextTime)
            )
        }

    val allAsteroids: LiveData<List<Asteroid>>
        get() = database.asteroidDAO.getAll()

    val todayAsteroid: LiveData<List<Asteroid>>
        get() {
            val calendar = Calendar.getInstance()
            val currentTime = calendar.time
            val dateFormat =
                SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            return database.asteroidDAO.getToday(dateFormat.format(currentTime))
        }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val apodValue =
                    NasaApi.service.getPictureOfDay()
                database.pictureofDayDAO.insert(apodValue.asDatabaseModel())
                Log.e("NASARepository", "Picture of Day Data Refreshed")
            } catch (e: Exception) {
                Log.e("NASARepository", e.message ?: "No internet Connection")
            }
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val calendar = Calendar.getInstance()
                val currentTime = calendar.time
                val dateFormat =
                    SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                val nextTime = calendar.time
                val asteroidsresult = NasaApi.service.getAsteroids(
                    dateFormat.format(currentTime),
                    dateFormat.format(nextTime)
                )
                val asteroids = parseStringToAsteroidList(asteroidsresult)
                database.asteroidDAO.insertAll(asteroids)
                Log.e("NASARepository", "Asteroid Data Refreshed")
            } catch (e: java.lang.Exception) {
                Log.e("NASARepository", e.message ?: "No internet Connection")
            }
        }
    }
}

