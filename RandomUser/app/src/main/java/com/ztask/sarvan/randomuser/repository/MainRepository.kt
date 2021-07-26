package com.ztask.sarvan.randomuser.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.ztask.sarvan.randomuser.MainApplication
import com.ztask.sarvan.randomuser.data.localdb.*
import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities
import com.ztask.sarvan.randomuser.data.model.RandomUserApiReponse
import com.ztask.sarvan.randomuser.data.model.ResultsItem
import com.ztask.sarvan.randomuser.data.model.WeatherApiResponse
import com.ztask.sarvan.randomuser.data.networkapi.ApiService
import com.ztask.sarvan.randomuser.data.networkapi.WeatherApiService
import com.ztask.sarvan.randomuser.utils.Constants
import com.ztask.sarvan.randomuser.viewmodel.MainViewModel

class MainRepository(val context: Context) {

    companion object {
        val TAG = MainRepository.javaClass.simpleName
    }

    suspend fun getWeatherData(
        latitude: String,
        longitude: String
    ): WeatherApiResponse {

        var response: WeatherApiResponse =
            MainApplication.weatherretrofit.create(WeatherApiService::class.java)
                .getWeatherInfo(latitude, longitude, Constants.WEATHER_API_KEY)
        return response
    }

    suspend fun getRandomUserData(count: Int): List<RandomUserEntities> {
        val dbHelper: DatabaseHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(context))
        if (dbHelper.getUsers().size <= 0) {
            var response: RandomUserApiReponse = MainApplication.retrofit.create(ApiService::class.java)
                .getUserList(count)

            Log.d(MainViewModel.TAG, "API Response1: " + Gson().toJson(response))
            saveUserData(dbHelper, response)
        }

        return dbHelper.getUsers()
    }

    private suspend fun saveUserData(dbHelper: DatabaseHelper, response: RandomUserApiReponse) {
        Log.d(MainViewModel.TAG, "API Response2: " + Gson().toJson(response))
        val itemList: List<ResultsItem?>? = response.results
        val insertItemList: MutableList<RandomUserEntities?>? = mutableListOf<RandomUserEntities?>()
        var uid = 0
        if (dbHelper.getUsers().size <= 0) {
            dbHelper.clearAll()
        }
        for (item in itemList!!) {
            ++uid
            var entities = RandomUserEntities(
                uid,
                item?.name?.title,
                item?.name?.first,
                item?.name?.last,
                item?.gender,
                item?.picture?.large,
                item?.cell,
                item?.email,
                item?.dob?.date?.substring(0, 10),
                item?.dob?.age.toString(),
                item?.location?.street?.number.toString(),
                item?.location?.street?.name,
                item?.location?.city,
                item?.location?.state,
                item?.location?.country,
                item?.location?.postcode.toString()
            )
            insertItemList?.add(entities)
            Log.d(TAG, "saveUserData: " + Gson().toJson(entities))
        }
        Log.d(TAG, "saveUserDataList: " + insertItemList?.size)
        Log.d(TAG, "saveUserDataList: " + Gson().toJson(insertItemList))
        dbHelper.insertAll(insertItemList)

    }
}