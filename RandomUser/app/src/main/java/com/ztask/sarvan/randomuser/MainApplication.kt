package com.ztask.sarvan.randomuser

import android.app.Application
import androidx.room.Room
import com.ztask.sarvan.randomuser.data.localdb.RandomUserDatabase
import com.ztask.sarvan.randomuser.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class MainApplication: Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    companion object {
        lateinit var retrofit: Retrofit
        lateinit var weatherretrofit: Retrofit
        lateinit var database: RandomUserDatabase
    }
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.APP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            weatherretrofit = Retrofit.Builder()
                .baseUrl(Constants.WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
          /*  database = Room
                .databaseBuilder(applicationContext, RandomUserDatabase::class.java, "db_users")
                .build()*/
        }
    }
}