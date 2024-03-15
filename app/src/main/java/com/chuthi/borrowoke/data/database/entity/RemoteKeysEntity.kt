package com.chuthi.borrowoke.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_table")
data class RemoteKeysEntity(
    @PrimaryKey
    val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)