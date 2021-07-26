package com.ztask.sarvan.randomuser.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities

@Dao
interface RandomUserDao {
    @Query("SELECT * FROM tbl_users")
    fun getAllUsers() : MutableList<RandomUserEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<RandomUserEntities?>?)

    @Query("DELETE FROM tbl_users")
    suspend fun clearAllUsers()

}