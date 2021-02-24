package com.udacity.asteroidradar.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Room.AsteroidDatabase
import com.udacity.asteroidradar.Room.asDomainModel
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseStringToAsteroidList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {

    suspend fun refreshDatabase() {
        withContext(Dispatchers.IO) {

            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val startDate = dateFormat.format(currentTime)
            try {
                val asteroidsList = Network.ApiService.getAsteroiS(
                    start_date = startDate,
                    api_key = Constants.API_Key
                )
                val NetworkAsteroidList = parseStringToAsteroidList(asteroidsList)
                asteroidDatabase.Dao.insertAsteroids(*NetworkAsteroidList.asDatabaseModel())
            } catch (exception: HttpException) {

            }
        }
    }

     suspend fun getImageoftheDay(): LiveData<PictureOfDay>? {
        val picOftheDay = Network.ApiService.getImageOfTheDay(Constants.API_Key)
        return if (picOftheDay.value?.mediaType.equals("image")) {
            picOftheDay
        } else null
    }

    val AsteroidList: LiveData<List<Asteroid>> = Transformations.map(asteroidDatabase.Dao.refreshAsteroidList()) {
            it.asDomainModel()

    }

}


