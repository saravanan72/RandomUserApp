package com.ztask.me.randomuser.data.localdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ztask.me.randomuser.data.localdb.Converters
import com.ztask.me.randomuser.data.localdb.ResultResponseElement
import com.ztask.me.randomuser.data.localdb.dao.RandomUserDao

@Database(
    entities = [ResultResponseElement::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RandomUserDatabase: RoomDatabase() {
    abstract fun getRandomUserDao(): RandomUserDao

    companion object{
        @Volatile private var instance: RandomUserDatabase? = null
        fun getDatabase(context: Context) : RandomUserDatabase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also{ instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, RandomUserDatabase::class.java, "randomusers")
                .fallbackToDestructiveMigration().build()
    }
}