package com.udacity.asteroidradar.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.PictureofDayDBModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String, val explanation: String) : Parcelable