package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.udacity.asteroidradar.work.FetchDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        delayedInit()
    }

    private fun delayedInit() =  applicationScope.launch { setUpRecurringWork() }


    private fun setUpRecurringWork() {
        val constraints=Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        var workRequest = PeriodicWorkRequestBuilder<FetchDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(FetchDataWorker.worker_name ,ExistingPeriodicWorkPolicy.KEEP ,workRequest)
    }


}
