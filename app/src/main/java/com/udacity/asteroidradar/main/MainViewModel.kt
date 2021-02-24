package com.udacity.asteroidradar.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Room.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)
    val repository = AsteroidRepository(database)
     var  asteroidsList:LiveData<List<Asteroid>>


    fun refereshDatabase() {
        viewModelScope.launch {
            repository.refreshDatabase()
        }
    }


    private val _image = MutableLiveData<PictureOfDay>()
   /* private val _asteroidsList =MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList
*/
    val image: LiveData<PictureOfDay>
        get() = _image
    init {
        refereshDatabase()
      asteroidsList=repository.AsteroidList
    // println(asteroidsList.value?.size.toString())

    }



    fun getImage() {

        viewModelScope.launch {
            _image.value = repository.getImageoftheDay()?.value
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}

