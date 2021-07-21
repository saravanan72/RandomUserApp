package com.ztask.me.randomuser.data.localdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ztask.me.randomuser.data.localdb.ResultResponseElement

@Dao
interface RandomUserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers() : LiveData<ResultResponseElement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<ResultResponseElement>)
}