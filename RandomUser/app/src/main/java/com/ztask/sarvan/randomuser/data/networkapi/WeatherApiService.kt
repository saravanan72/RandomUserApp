package com.ztask.sarvan.randomuser.data.networkapi

import com.ztask.sarvan.randomuser.data.model.WeatherApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeatherInfo(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("APPID") appid: String,
    ): WeatherApiResponse
}