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

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.models.PictureOfDay

@Entity
data class PictureofDayDBModel(
    val date: String,
    val explanation: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    @PrimaryKey
    val url: String
) {
    fun asModel(): PictureOfDay {
        return PictureOfDay(
            media_type, title, url, explanation
        )
    }
}

