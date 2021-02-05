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

@Dao
interface NasaDAO {
    @Query("select * from PictureofDayDBModel order by date limit 1")
    fun getPictureOfDay(): LiveData<PictureofDayDBModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pictureofDayDBModel: PictureofDayDBModel)
}

@Database(entities = [PictureofDayDBModel::class], version = 2)
abstract class NasaDatabase : RoomDatabase() {
    abstract val dao: NasaDAO
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