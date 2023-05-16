package com.chuthi.borrowoke.data.model.response

data class Choice(
    val finish_reason: String,
    val index: Int,
    val message: Message
)