package com.ztask.sarvan.randomuser.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ztask.sarvan.randomuser.data.localdb.dao.RandomUserDao
import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities

@Database(
    entities =[RandomUserEntities::class],
    version = 1,
    exportSchema = false
)
abstract class RandomUserDatabase: RoomDatabase() {
    abstract fun getRandomUserDao() : RandomUserDao

}