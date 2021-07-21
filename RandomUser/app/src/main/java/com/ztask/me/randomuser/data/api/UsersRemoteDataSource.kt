package com.ztask.me.randomuser.data.api

import javax.inject.Inject

class UsersRemoteDataSource @Inject constructor(
    private val apiService: ApiService
): BaseDataSource() {
    suspend fun getUserList() = getResult { apiService.getUserList(25) }
}