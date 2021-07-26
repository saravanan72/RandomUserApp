package com.ztask.sarvan.randomuser.data.localdb

import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities

class DatabaseHelperImpl(private val randomUserDatabase: RandomUserDatabase): DatabaseHelper {

    override suspend fun getUsers(): MutableList<RandomUserEntities>  =
        randomUserDatabase.getRandomUserDao().getAllUsers()

    override suspend fun insertAll(users: List<RandomUserEntities?>?) =
        randomUserDatabase.getRandomUserDao().insertAllUsers(users)

    override suspend fun clearAll() {
        randomUserDatabase.getRandomUserDao().clearAllUsers()
    }
}