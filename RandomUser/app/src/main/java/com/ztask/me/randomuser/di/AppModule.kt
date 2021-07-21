package com.ztask.me.randomuser.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ztask.me.randomuser.data.api.ApiService
import com.ztask.me.randomuser.data.api.UsersRemoteDataSource
import com.ztask.me.randomuser.data.localdb.dao.RandomUserDao
import com.ztask.me.randomuser.data.localdb.db.RandomUserDatabase
import com.ztask.me.randomuser.data.repository.RandomUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://randomuser.me/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService =
            retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideuserRemoteDataSource(apiService: ApiService) = UsersRemoteDataSource(apiService)

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext appContext: Context) = RandomUserDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideUserDao(db: RandomUserDatabase) = db.getRandomUserDao()

    @Singleton
    @Provides
    fun provideUserRepository(remoteDataSource: UsersRemoteDataSource, userDao: RandomUserDao) =
            RandomUserRepository(remoteDataSource, userDao)
}