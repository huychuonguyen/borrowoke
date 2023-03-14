package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthModel(
    val name: String = ""
) : BaseModel()
