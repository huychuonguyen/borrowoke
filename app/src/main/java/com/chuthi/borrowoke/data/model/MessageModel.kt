package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.data.model.response.ChatGptMessage

sealed class GptUserRole(val role: String) {
    object User : GptUserRole("user")
    object Assistant : GptUserRole("assistant")
    object System : GptUserRole("system")
}

data class MessageModel(
    val id: String,
    val role: GptUserRole,
    val content: String, // this is the message
    val date: String,
    var isVisibleDate: Boolean = false,
    val url: String? = null
) : BaseModel()

fun MessageModel.toChatGPTMessage(): ChatGptMessage {
    val role = when (role) {
        is GptUserRole.User -> ChatGptMessage.ROLE_USER
        is GptUserRole.Assistant -> ChatGptMessage.ROLE_ASSISTANT
        is GptUserRole.System -> ChatGptMessage.ROLE_SYSTEM
    }
    return ChatGptMessage(
        role = role,
        content = content
    )
}