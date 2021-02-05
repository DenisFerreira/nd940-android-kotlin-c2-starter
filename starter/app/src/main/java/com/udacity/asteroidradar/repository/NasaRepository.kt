package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.NasaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NasaRepository(private val database: NasaDatabase) {
    val pictureOfDay: LiveData<PictureOfDay>
        get() = database.dao.getPictureOfDay().map { it?.asModel() ?: PictureOfDay("","", "")  }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            try {
                val apodValue = NasaApi.RETROFIT_SERVICE.getAPOD("xsfeJEdHdBgIgCIkEp9skjoMYh6JKAbbIcfCkZS1").await()
                database.dao.insert(apodValue.asDatabaseModel())
            }catch (e: Exception) {
                Log.e("NASARepository", e.message?:"No internet Connection")
            }
        }
    }
}

