package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.data.database.entity.UserEntity
import com.chuthi.borrowoke.other.enums.UiText

data class UserModel(
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val value: UiText = UiText.Empty
) : BaseModel()

fun UserModel.toUserEntity() = UserEntity(
    id = id,
    userId = userId,
    name = name
)
