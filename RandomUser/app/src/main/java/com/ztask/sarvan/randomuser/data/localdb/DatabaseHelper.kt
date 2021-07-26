package com.ztask.sarvan.randomuser.data.localdb

import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities

interface DatabaseHelper {

    suspend fun getUsers(): MutableList<RandomUserEntities>

    suspend fun insertAll(users: List<RandomUserEntities?>?)

    suspend fun clearAll()
}