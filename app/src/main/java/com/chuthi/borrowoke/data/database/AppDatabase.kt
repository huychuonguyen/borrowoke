package com.chuthi.borrowoke.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chuthi.borrowoke.data.database.dao.RemoteKeysDao
import com.chuthi.borrowoke.data.database.dao.UserDao
import com.chuthi.borrowoke.data.database.entity.RemoteKeysEntity
import com.chuthi.borrowoke.data.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        RemoteKeysEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
