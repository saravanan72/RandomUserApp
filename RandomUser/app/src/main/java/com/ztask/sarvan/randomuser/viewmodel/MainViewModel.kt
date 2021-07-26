package com.ztask.sarvan.randomuser.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities
import com.ztask.sarvan.randomuser.data.model.WeatherApiResponse
import com.ztask.sarvan.randomuser.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(val context: Context) : ViewModel() {

    companion object{
        val TAG: String = MainViewModel.javaClass.simpleName.toString()
    }
    var weatherReponse =  MutableLiveData<WeatherApiResponse>()
    var userReponse =  MutableLiveData<List<RandomUserEntities>>()
    val repository: MainRepository = MainRepository(context)

    val weatherInfo: LiveData<WeatherApiResponse> get() = weatherReponse

    val userApiResponse: LiveData<List<RandomUserEntities>> get() = userReponse

    fun getWeatherDetails(latitude: String, longitude: String) = viewModelScope.launch {
        var reponse: WeatherApiResponse = repository.getWeatherData(latitude, longitude)
        weatherReponse.postValue(reponse)
    }

    fun getRandomUserDetails() = viewModelScope.launch {
        var response: List<RandomUserEntities> = repository.getRandomUserData(100)
        Log.d(TAG, "DBResponse: "+Gson().toJson(response))
        userReponse.postValue(response)
    }
}