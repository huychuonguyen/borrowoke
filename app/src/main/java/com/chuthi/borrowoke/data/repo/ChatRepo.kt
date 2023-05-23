package com.chuthi.borrowoke.data.repo

import com.chuthi.borrowoke.base.BaseRepo
import com.chuthi.borrowoke.data.api.ApiService
import com.chuthi.borrowoke.data.model.request.ChatGptRequest
import com.chuthi.borrowoke.data.model.request.GptCompletionRequest

class ChatRepo(private val apiService: ApiService) : BaseRepo() {

    suspend fun gptChatCompletions(request: ChatGptRequest) = apiService.gptChatCompletions(request)
    suspend fun gptCompletions(request: GptCompletionRequest) = apiService.gptCompletions(request)
}