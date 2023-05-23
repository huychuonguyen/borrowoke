package com.chuthi.borrowoke.data.model.response

import com.chuthi.borrowoke.data.model.GptUserRole
import com.chuthi.borrowoke.data.model.MessageModel
import com.chuthi.borrowoke.util.getCurrentDate
import kotlin.random.Random

data class Choice(
    val finish_reason: String,
    val index: Int,
    val message: ChatGptMessage,
    val logprobs: Any? = null,
    val text: String
)

/**
 * This extension used for api:  @POST("v1/completions")
 */
fun Choice.toMessageModel(): MessageModel {
    return MessageModel(
        id = Random.nextInt().toString(),
        date = getCurrentDate(),
        content = text.trim(),
        role = GptUserRole.Assistant
    )
}