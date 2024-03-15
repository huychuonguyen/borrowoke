package com.chuthi.borrowoke.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chuthi.borrowoke.data.database.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys_table WHERE id = :id")
    suspend fun remoteKeysRepoId(id: Int): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys_table")
    suspend fun clearRemoteKeys()
}