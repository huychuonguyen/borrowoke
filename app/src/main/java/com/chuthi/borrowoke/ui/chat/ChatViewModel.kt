package com.chuthi.borrowoke.ui.chat

import androidx.lifecycle.asLiveData
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.GptUserRole
import com.chuthi.borrowoke.data.model.MessageModel
import com.chuthi.borrowoke.data.model.request.ChatGptRequest
import com.chuthi.borrowoke.data.model.request.GptCompletionRequest
import com.chuthi.borrowoke.data.model.response.toMessageModel
import com.chuthi.borrowoke.data.model.toChatGPTMessage
import com.chuthi.borrowoke.data.repo.ChatRepo
import com.chuthi.borrowoke.ext.apiCall
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.ApiResponse
import com.chuthi.borrowoke.other.enums.ChatUiState
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.MessageType
import com.chuthi.borrowoke.other.enums.UiText
import com.chuthi.borrowoke.util.getCurrentDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ChatViewModel(
    private val chatRepo: ChatRepo
) : BaseViewModel() {

    private val _validInput = MutableStateFlow(false)
    val validInput = _validInput.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageModel>>(listOf())
    val messages = _messages.asStateFlow()

    private val _chatUiState = MutableSharedFlow<ChatUiState>()
    val chatUiState = flow {
        _chatUiState.collect {
            emit(it)
        }
    }.asLiveData()


    private val _recentTokens = MutableStateFlow(0)
    val recentTokens = _recentTokens.asStateFlow()

    private val _totalTokens = MutableStateFlow(0)
    val totalTokens = _totalTokens.map {
        delay(300)
        it
    }

    val cost = _totalTokens.map {
        it * (0.002 / 1000)
    }

    fun sendQuestion(question: String) =
        sendChatQuestion(question) // sendCompletionQuestion(question)

    /**
     * Using for chat
     */
    private fun sendChatQuestion(question: String) = launchViewModelScope {
        // first add question mess
        val questionMessage = addQuestionMessage(question)
        // show loading
        _chatUiState.emit(ChatUiState.Loading(true))

        // create request model
        val chatGptRequest = ChatGptRequest(
            messages = messages.value.map {
                it.toChatGPTMessage()
            }
        )
        // then api call
        chatRepo.gptChatCompletions(chatGptRequest).apiCall(
            onSuccess = {
                val response = it.data
                // emit tokens
                val tokens = response?.usage?.total_tokens ?: 0
                _recentTokens.emit(tokens)
                _totalTokens.emit(_totalTokens.value + tokens)

                val responseMessages = response?.choices?.map { choice ->
                    choice.message.toMessageModel()
                }
                responseMessages ?: return@apiCall

                responseMessages.forEach { message ->
                    // show response
                    addAssistantMessage(
                        message.content
                    )
                }
                _chatUiState.emit(ChatUiState.NewMessage(MessageType.Response))
                _chatUiState.emit(ChatUiState.Loading(false))
            }, onError = {
                delay(500)
                // remove message already added
                removeMessage(message = questionMessage)
                // handle error
                handleError(it)
            }, onFinished = {
            }
        )
    }

    /**
     * Using for only 1 question,
     * it means every question must description an context by its self
     */
    private fun sendCompletionQuestion(question: String) = launchViewModelScope {
        // first add question mess
        val messageModel = MessageModel(
            id = Random.nextInt().toString(),
            role = GptUserRole.User,
            content = question.trim(),
            date = getCurrentDate()
        )
        //addQuestionMessage(question)
        _messages.emit(messages.value.plus(messageModel))
        // show loading
        _chatUiState.emit(ChatUiState.Loading(true))
        delay(100)
        _chatUiState.emit(ChatUiState.NewMessage(MessageType.Question))

        // create request model
        val chatGptRequest = GptCompletionRequest(
            prompt = question.trim()
        )
        // then api call
        chatRepo.gptCompletions(chatGptRequest).apiCall(
            onSuccess = {
                val response = it.data
                val firstResponseMessage = response?.choices?.map { choice ->
                    choice.toMessageModel()
                }

                // emit tokens
                val tokens = response?.usage?.total_tokens ?: 0
                _recentTokens.emit(tokens)
                _totalTokens.emit(_totalTokens.value + tokens)

                firstResponseMessage ?: return@apiCall

                firstResponseMessage.forEach { message ->
                    // show response
                    addAssistantMessage(message.content)
                    delay(100)
                }

            },
            onError = {
                val errorMess = it.message
                val errorCode = it.errorCode
                commonError.emit(
                    CommonError.NormalError(
                        message = errorMess, code = errorCode
                    )
                )
            }, onFinished = {
                _chatUiState.emit(ChatUiState.Loading(false))
            }
        )
    }

    /**
     * Add new question message.
     * @return [MessageModel]
     */
    private suspend fun addQuestionMessage(question: String) = MessageModel(
        id = Random.nextInt().toString(),
        role = GptUserRole.User,
        content = question.trim(),
        date = getCurrentDate()
    ).apply {
        addMessage(this, type = MessageType.Question)
    }

    private suspend fun addAssistantMessage(responseContent: String) = MessageModel(
        id = Random.nextInt().toString(),
        role = GptUserRole.Assistant,
        content = responseContent.trim(),
        date = getCurrentDate()
    ).apply {
        addMessage(this, type = MessageType.Response)
    }

    private suspend fun addMessage(newMessage: MessageModel, type: MessageType) {
        _messages.emit(messages.value.plus(newMessage))
        // _chatUiState.emit(ChatUiState.NewMessage(type))
    }

    private fun removeMessage(message: MessageModel) = _messages.update { list ->
        list.filter {
            it.id != message.id
        }
    }

    fun validateInput(question: String?) = launchViewModelScope {
        if (question.isNullOrEmpty() || question.isBlank()) _validInput.emit(false)
        else _validInput.emit(true)
    }

    fun setItemVisibleDate(
        item: MessageModel,
        isVisible: Boolean
    ) = launchViewModelScope {
        val updatedMessages = messages.value.map {
            if (it.id != item.id) it
            else item.copy(isVisibleDate = isVisible)
        }
        _messages.emit(updatedMessages)
    }

    fun resetChat() = launchViewModelScope {
        _recentTokens.emit(0)
        _totalTokens.emit(0)
        _chatUiState.emit(ChatUiState.Resetting(message = UiText.StringResource(R.string.reset_chat)))
        _chatUiState.emit(ChatUiState.NewMessage(MessageType.Question))
        delay(1500)
        _messages.update { listOf() }
        _chatUiState.emit(ChatUiState.Loading(false))
    }

    //========================================================

    /**
     * Get dummy message list.
     */
    private fun getDummyMessages() = mutableListOf<MessageModel>().apply {
        for (i in 0 until 20) {
            add(
                MessageModel(
                    id = Random.nextInt().toString(),
                    role = if (i % 2 == 0) GptUserRole.Assistant else GptUserRole.User,
                    content = "Xin chào! Tao giúp gì được cho mày?",
                    date = getCurrentDate()
                ),
            )
        }
    }

    private fun handleError(error: CommonError) = launchViewModelScope {
        val (icSrc, message) = when (error) {
            is ApiResponse.NetworkError<*> -> Pair(
                R.drawable.ic_lost_connection,
                UiText.StringResource(R.string.no_internet_connection)
            )

            else -> Pair(R.drawable.ic_error, error.message)
        }
        _chatUiState.emit(
            ChatUiState.LoadError(
                src = icSrc,
                message = message
            )
        )
    }
}
