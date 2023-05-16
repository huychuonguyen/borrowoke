package com.chuthi.borrowoke.data.model.response

data class ChatGptResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)