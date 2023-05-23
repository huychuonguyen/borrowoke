package com.chuthi.borrowoke.data.model.request

import com.chuthi.borrowoke.data.model.response.ChatGptMessage

data class ChatGptRequest(
    val messages: List<ChatGptMessage>,
    val model: String = "gpt-3.5-turbo",
    //val temperature: Double = 0.7
)

