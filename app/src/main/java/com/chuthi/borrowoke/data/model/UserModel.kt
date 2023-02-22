package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.base.BaseModel

data class UserModel(
    val userId: Int,
    val name: String,
) : BaseModel()