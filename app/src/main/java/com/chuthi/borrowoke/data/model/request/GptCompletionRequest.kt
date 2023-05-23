package com.chuthi.borrowoke.data.model.request

data class GptCompletionRequest(
    val logprobs: Any? = null,
    val max_tokens: Int = 1000,
    val model: String = "text-davinci-003",
    //val n: Int = 1, // How many completions to generate for each prompt.
    val prompt: String,
    //val stop: String = "\n",
    //val stream: Boolean = false,
    val temperature: Int = 0,
    //val top_p: Int = 1
)