package com.chuthi.borrowoke.data.repo

import com.chuthi.borrowoke.base.BaseRepo
import com.chuthi.borrowoke.data.api.ApiService
import com.chuthi.borrowoke.data.model.request.ChatGptRequest

class ChatRepo(private val apiService: ApiService) : BaseRepo() {

    suspend fun chatGPTCompletions(request: ChatGptRequest) = apiService.chatGPTCompletions(request)
}