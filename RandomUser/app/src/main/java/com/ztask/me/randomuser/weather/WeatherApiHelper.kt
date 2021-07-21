package com.ztask.me.randomuser.weather

import retrofit2.Response

interface WeatherApiHelper {
    suspend fun getWeather(country: String): Response<WeatherResponse>
    suspend fun getWeatherLatLong(latitude: String,longitude: String): Response<WeatherResponse>
}