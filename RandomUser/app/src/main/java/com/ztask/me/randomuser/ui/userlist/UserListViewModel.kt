package com.ztask.me.randomuser.ui.userlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ztask.me.randomuser.data.repository.RandomUserRepository
import com.ztask.me.randomuser.utils.Resource
import com.ztask.me.randomuser.weather.WeatherApiHelper
import com.ztask.me.randomuser.weather.WeatherResponse
import kotlinx.coroutines.launch

class UserListViewModel @ViewModelInject constructor(
        private val repository: RandomUserRepository,
        private val apiHelper: WeatherApiHelper
) : ViewModel() {

    val users = repository.getUserList()

    private val _res = MutableLiveData<Resource<WeatherResponse>>()

    val res: LiveData<Resource<WeatherResponse>>
        get() = _res

    fun getWeatherDataLatLong(latitude: String, longitude: String) = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        repository.getWeatherApiLatLong(apiHelper, latitude, longitude).let {
            if (it.isSuccessful) {
                _res.postValue(Resource.success(it.body()) as Resource<WeatherResponse>?)
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }
}