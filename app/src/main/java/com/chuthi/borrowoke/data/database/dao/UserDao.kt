package com.chuthi.borrowoke.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chuthi.borrowoke.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY userId ASC")
    fun getUsersOrderByName(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user_table ORDER BY userId ASC")
    fun getUserPaging(): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM user_table where userId = :userId")
    fun getUserById(userId: Int): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user_table WHERE userId = :userId")
    suspend fun deleteUser(userId: Int)
}