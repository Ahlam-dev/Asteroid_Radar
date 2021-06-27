package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Room.AsteroidDatabase
import com.udacity.asteroidradar.Room.asDomainModel
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseStringToAsteroidList
import com.udacity.asteroidradar.main.AsteroidListOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {
    suspend fun deleteAsteroids() {
        withContext(Dispatchers.IO) {
            val cal = Calendar.getInstance()
            cal.time = cal.time
            cal.add(Calendar.DAY_OF_MONTH, -1) //Goes to previous day
            val yesterdayTime = cal.time

            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

            val yesterdayDate = dateFormat.format(yesterdayTime)

            asteroidDatabase.Dao.deleteAsteroids(yesterdayDate.toString())
        }

    }


    suspend fun refreshDatabase() {
        withContext(Dispatchers.IO) {

            val startDate = getCurrentDate()
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

    private fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val startDate = dateFormat.format(currentTime)
        return startDate
    }

    suspend fun getImageoftheDay(): LiveData<PictureOfDay>? {
        val picOftheDay = MutableLiveData<PictureOfDay>()
        picOftheDay.value = Network.ApiService.getImageOfTheDay(Constants.API_Key)
        return if (picOftheDay.value?.mediaType.equals("image")) {
            picOftheDay
        } else null
    }

    val AsteroidList: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.Dao.refreshAsteroidList()) {
            it.asDomainModel()

        }

    fun filterList(filter: AsteroidListOptions): LiveData<List<Asteroid>> {
        return when (filter) {
            AsteroidListOptions.Show_today -> {Transformations.map(
                asteroidDatabase.Dao.getTodayAsteroids(
                    getCurrentDate()
                )
            ) {
                it.asDomainModel()
            }}
            AsteroidListOptions.Show_saved -> {Transformations.map(asteroidDatabase.Dao.refreshAsteroidList()) {
                it.asDomainModel()
            }}

            else -> {Transformations.map(asteroidDatabase.Dao.getWeekAsteroids(getCurrentDate())) {
                it.asDomainModel()
            }
        }}
    }


}


