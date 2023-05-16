package com.chuthi.borrowoke.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.request.ChatGptRequest
import com.chuthi.borrowoke.data.model.request.MessageRequest
import com.chuthi.borrowoke.data.model.response.ChatGptResponse
import com.chuthi.borrowoke.data.repo.ChatRepo
import com.chuthi.borrowoke.ext.apiCall
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel(private val chatRepo: ChatRepo) : BaseViewModel() {

    private val _validInput = MutableStateFlow(false)
    val validInput = _validInput.asStateFlow()

    private val _chatGptResponse = MutableLiveData<ChatGptResponse>()
    val chatGptResponse: LiveData<ChatGptResponse> = _chatGptResponse

    init {
        //askChatGpt(question = "Hi, What time is it in Viet Nam?")
    }

    fun askChatGpt(question: String) = launchViewModelScope {
        showLoading()

        val chatGptRequest = ChatGptRequest(
            messages = listOf(
                MessageRequest(
                    content = question
                )
            )
        )

        chatRepo.chatGPTCompletions(chatGptRequest).apiCall(
            onSuccess = {
                val response = it.data
            },
            onError = {
                val errorMess = it.message ?: ""
                val errorCode = it.errorCode
                commonError.emit(
                    CommonError.NormalError(
                        message = UiText.DynamicString(errorMess), code = errorCode
                    )
                )
            },
            onFinished = {
                hideLoading()
            }
        )
    }

    fun validateInput(question: String?) = launchViewModelScope {
        if (question.isNullOrEmpty()) _validInput.emit(false)
        else _validInput.emit(true)
    }
}