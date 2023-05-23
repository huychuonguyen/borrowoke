package com.chuthi.borrowoke.data.model.response

data class ChatGptResponse(
    val choices: List<Choice>,
    val created: Long,
    val id: String,
    val `object`: String,
    val usage: Usage
)