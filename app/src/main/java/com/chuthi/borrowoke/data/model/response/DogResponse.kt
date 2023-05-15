package com.chuthi.borrowoke.data.model.response

import com.chuthi.borrowoke.base.BaseModel

data class DogResponse(
    val breeds: List<Any>,
    val categories: List<Any>,
    val id: String,
    val url: String
) : BaseModel()