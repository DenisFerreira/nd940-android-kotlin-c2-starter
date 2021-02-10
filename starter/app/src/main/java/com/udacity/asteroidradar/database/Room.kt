/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.models.Asteroid

@Dao
interface PictureofDayDAO {
    @Query("select * from PictureofDayDBModel order by date limit 1")
    fun getPictureOfDay(): LiveData<PictureofDayDBModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pictureofDayDBModel: PictureofDayDBModel)
}

@Dao
interface AsteroidDAO {

    @Query("select * from Asteroid where closeApproachDate between :dateInit and :dateEnd")
    fun getAll(dateInit: String, dateEnd: String): LiveData<List<Asteroid>>

    @Query("select * from Asteroid")
    fun getAll(): LiveData<List<Asteroid>>

    @Query("select * from Asteroid where closeApproachDate = :date")
    fun getToday(date: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<Asteroid>)
}

@Database(entities = [PictureofDayDBModel::class, Asteroid::class], version = 1)
abstract class NasaDatabase : RoomDatabase() {
    abstract val pictureofDayDAO: PictureofDayDAO
    abstract val asteroidDAO: AsteroidDAO
}

private lateinit var INSTANCE: NasaDatabase

fun getDatabase(context: Context): NasaDatabase {
    synchronized(NasaDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    NasaDatabase::class.java,
                    "nasa").build()
        }
    }
    return INSTANCE
}