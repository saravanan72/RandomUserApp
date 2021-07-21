package com.ztask.me.randomuser.data.repository

import com.ztask.me.randomuser.data.api.UsersRemoteDataSource
import com.ztask.me.randomuser.data.localdb.dao.RandomUserDao
import com.ztask.me.randomuser.utils.performGetOperation
import com.ztask.me.randomuser.weather.WeatherApiHelper
import javax.inject.Inject

class RandomUserRepository @Inject constructor(
    private val dataSource: UsersRemoteDataSource,
    private val randomUserDao: RandomUserDao
) {
    fun getUserList() = performGetOperation(
        databaseQuery = { randomUserDao.getAllUsers() },
        networkCall = { dataSource.getUserList() },
        saveCallResult = { randomUserDao.insertAllUsers(it) }
    )
    suspend fun getWeatherApiLatLong(apiHelper: WeatherApiHelper, latitude: String,longitude: String) = apiHelper.getWeatherLatLong(latitude,longitude)
}