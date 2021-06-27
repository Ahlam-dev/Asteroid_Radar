package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Room.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class FetchDataWorker(appcontext: Context, parameters: WorkerParameters) :
    CoroutineWorker(appcontext, parameters) {
companion object {
    const val worker_name="FetchDataWorker"
}
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshDatabase()
            repository.deleteAsteroids()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }


}