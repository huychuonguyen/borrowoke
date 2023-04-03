package com.chuthi.borrowoke.other.enums

sealed class CommonError(
    open val message: UiText
) {
    object EmptyError : CommonError(UiText.Empty)

    data class NormalError(override val message: UiText, val code: Int) : CommonError(message)
}