package com.chuthi.borrowoke.data.model.response

import com.chuthi.borrowoke.data.model.GptUserRole
import com.chuthi.borrowoke.data.model.MessageModel
import com.chuthi.borrowoke.util.getCurrentDate
import kotlin.random.Random

data class ChatGptMessage(
    val content: String,
    val role: String = "user"
) {
    companion object {
        const val ROLE_USER = "user"
        const val ROLE_ASSISTANT = "assistant"
        const val ROLE_SYSTEM = "system"
    }
}

/**
 * This extension used for api:  @POST("v1/chat/completions")
 */
fun ChatGptMessage.toMessageModel(): MessageModel {
    val role = when (this.role) {
        ChatGptMessage.ROLE_USER -> GptUserRole.User
        ChatGptMessage.ROLE_ASSISTANT -> GptUserRole.Assistant
        else -> GptUserRole.System
    }
    return MessageModel(
        id = Random.nextInt().toString(),
        date = getCurrentDate(),
        content = content,
        role = role
    )
}