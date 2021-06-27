package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Room.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
enum class AsteroidListOptions{Show_saved,Show_week,Show_today}
class MainViewModel(application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)
    val repository = AsteroidRepository(database)

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList
    private val asteroidListObserver = Observer<List<Asteroid>> {
        _asteroidList.value = it
    }
    private var asteroidListLiveData: LiveData<List<Asteroid>>

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid


    private val _imageOFtheDay = MutableLiveData<PictureOfDay>()

    val imageOFtheDay: LiveData<PictureOfDay>
        get() = _imageOFtheDay


    init {
        asteroidListLiveData =
            repository.filterList(AsteroidListOptions.Show_saved)
        asteroidListLiveData.observeForever(asteroidListObserver)

        refereshDatabase()
        getImage()
    }

    fun refereshDatabase() {
        viewModelScope.launch {

            repository.refreshDatabase()
        }
    }


    fun getImage() {

        viewModelScope.launch {
            _imageOFtheDay.value = repository.getImageoftheDay()?.value
        }
    }

    fun displayDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid

    }

    fun doneDisplayDetail() {
        _navigateToSelectedAsteroid.value = null
    }

    fun updateFilter(filter: AsteroidListOptions) {
        asteroidListLiveData =
            repository.filterList(filter)
        asteroidListLiveData.observeForever(asteroidListObserver)
    }
    override fun onCleared() {
        super.onCleared()
        asteroidListLiveData.removeObserver(asteroidListObserver)
    }




    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }}


