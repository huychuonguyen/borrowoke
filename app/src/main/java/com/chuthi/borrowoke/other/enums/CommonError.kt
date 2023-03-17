package com.chuthi.borrowoke.other.enums

sealed class CommonError(
    open val message: UiText
) {
    object ErrorEmpty : CommonError(UiText.Empty)

    data class ErrorNormal(override val message: UiText) : CommonError(message)
}