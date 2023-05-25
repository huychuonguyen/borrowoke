package com.chuthi.borrowoke.other.enums

import androidx.annotation.DrawableRes
import com.chuthi.borrowoke.R

/**
 * ChatUiState.
 */
sealed interface ChatUiState {
    data class Loading(val isLoading: Boolean = false) : ChatUiState
    object None : ChatUiState
    data class NewMessage(val type: MessageType) : ChatUiState
    data class LoadError(
        @DrawableRes val src: Int = R.drawable.ic_error,
        val message: UiText
    ) : ChatUiState

    data class Resetting(val message: UiText) : ChatUiState
}

sealed interface MessageType {
    object Response : MessageType
    object Question : MessageType
}