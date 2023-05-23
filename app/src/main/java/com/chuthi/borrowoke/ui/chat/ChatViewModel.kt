package com.chuthi.borrowoke.ui.chat

import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.GptUserRole
import com.chuthi.borrowoke.data.model.MessageModel
import com.chuthi.borrowoke.data.model.request.ChatGptRequest
import com.chuthi.borrowoke.data.model.request.GptCompletionRequest
import com.chuthi.borrowoke.data.model.response.toMessageModel
import com.chuthi.borrowoke.data.model.toChatGPTMessage
import com.chuthi.borrowoke.data.repo.ChatRepo
import com.chuthi.borrowoke.data.repo.DogRepo
import com.chuthi.borrowoke.ext.apiCall
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.ChatUiState
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.MessageType
import com.chuthi.borrowoke.other.enums.UiText
import com.chuthi.borrowoke.util.getCurrentDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val dogRepo: DogRepo
) : BaseViewModel() {

    private val _validInput = MutableStateFlow(false)
    val validInput = _validInput.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageModel>>(listOf())
    val messages = _messages.asStateFlow()

    private val _chatUiState = MutableStateFlow<ChatUiState>(ChatUiState.None)
    val chatUiState = _chatUiState.asStateFlow()

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


    private var responseCount = 0

    init {
        //getMessages(getFirstMessage())
    }

    fun sendQuestion(question: String) =
        sendChatQuestion(question) // sendCompletionQuestion(question)

    /**
     * Using for chat
     */
    private fun sendChatQuestion(question: String) = launchViewModelScope {
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
                    addNewMessage(message, MessageType.Response)
                    delay(100)
                }

            },
            onError = {
                commonError.emit(it)
            }, onFinished = {
                _chatUiState.emit(ChatUiState.Loading(false))
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
                    addNewMessage(message, MessageType.Response)
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

    fun sendQuestion2(question: String) = launchViewModelScope {
        _chatUiState.emit(ChatUiState.Loading(true))
        addQuestionMessage(question)

        responseCount++
        if (responseCount % 3 != 0) {
            addAssistantMessage(SAMPLE_RESPONSE)
        } else {
            delay(1000)
            val errorMess = UiText.StringResource(R.string.assistant_can_not_response)
            _chatUiState.emit(ChatUiState.LoadError(message = errorMess))
        }
    }

    private fun addQuestionMessage(question: String) {

        val messageModel = MessageModel(
            id = Random.nextInt().toString(),
            role = GptUserRole.User,
            content = question.trim(),
            date = getCurrentDate()
        )

        addNewMessage(messageModel, type = MessageType.Question)
    }

    private fun addAssistantMessage(responseContent: String) = launchViewModelScope {
        dogRepo.getDogs(limit = 1).apiCall(
            onSuccess = {
                val dogUrl = it.data?.firstOrNull()?.url
                val messageModel = MessageModel(
                    id = Random.nextInt().toString(),
                    role = GptUserRole.Assistant,
                    content = responseContent.trim(),
                    date = getCurrentDate(),
                    url = dogUrl
                )

                addNewMessage(messageModel, type = MessageType.Response)
            },
            onError = {
                val errorMess = UiText.StringResource(R.string.assistant_can_not_response)
                _chatUiState.emit(ChatUiState.LoadError(message = errorMess))
            },
            onFinished = {
                _chatUiState.emit(ChatUiState.Loading(false))
            }
        )

    }

    private fun addNewMessage(newMessage: MessageModel, type: MessageType) = launchViewModelScope {
        _messages.emit(messages.value.plus(newMessage))
        _chatUiState.emit(ChatUiState.NewMessage(type))
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
        delay(100)
        _chatUiState.emit(ChatUiState.NewMessage(MessageType.Question))
        delay(1500)
        getMessages(listOf())
        _chatUiState.emit(ChatUiState.Loading(false))
    }

    //========================================================

    private fun getMessages(messages: List<MessageModel>) = launchViewModelScope {
        _messages.emit(messages)
    }

    /**
     * Get dummy message list.
     */
    private fun getFirstMessage() = mutableListOf<MessageModel>().apply {
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


    companion object {
        private const val SAMPLE_RESPONSE = "Tao không biết \uD83E\uDD23,\nxem hình chó đỡ đi 👽"
    }
}