package com.chuthi.borrowoke.data.model.request

data class ChatGptRequest(
    val messages: List<MessageRequest>,
    val model: String = "gpt-3.5-turbo",
    //val temperature: Double = 0.7
)

data class MessageRequest(
    val content: String,
    val role: String = "user"
)