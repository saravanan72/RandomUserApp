package com.ztask.sarvan.randomuser.data.localdb

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: RandomUserDatabase? = null

    fun getInstance(context: Context): RandomUserDatabase {
        if (INSTANCE == null) {
            synchronized(RandomUserDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            RandomUserDatabase::class.java,
            "db_randomusers"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

}