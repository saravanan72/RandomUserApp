package com.ztask.me.randomuser.data.api

import com.ztask.me.randomuser.data.localdb.ResultResponseElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    suspend fun getUserList(
        @Query("results") results: Int
    ): Response<List<ResultResponseElement>>
}