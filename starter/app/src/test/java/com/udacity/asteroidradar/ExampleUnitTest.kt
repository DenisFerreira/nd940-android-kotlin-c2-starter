package com.udacity.asteroidradar

import com.udacity.asteroidradar.api.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testNeoFeedRequest() {
        val calendar = Calendar.getInstance()
        runBlocking {
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val date = dateFormat.format(currentTime)
            val neoFeed = NasaApi.service.getNeoFeed(date,"xsfeJEdHdBgIgCIkEp9skjoMYh6JKAbbIcfCkZS1").await()
            val listAsteroids = parseAsteroidsJsonResult(neoFeed)
            assert(listAsteroids.isNotEmpty())
        }
    }
}
