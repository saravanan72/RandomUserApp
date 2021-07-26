package com.ztask.sarvan.randomuser.data.networkapi

import com.ztask.sarvan.randomuser.data.model.RandomUserApiReponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    suspend fun getUserList(
        @Query("results") results: Int
    ): RandomUserApiReponse
}