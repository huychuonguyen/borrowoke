package com.chuthi.borrowoke.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chuthi.borrowoke.data.database.dao.UserDao
import com.chuthi.borrowoke.data.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
