package com.chuthi.borrowoke.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.data.model.UserModel

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val name: String,
) : BaseModel()

fun UserEntity.toUserModel() = UserModel(
    id = id,
    userId = userId,
    name = name
)
