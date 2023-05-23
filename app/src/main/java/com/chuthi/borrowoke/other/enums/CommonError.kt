package com.chuthi.borrowoke.other.enums

import com.chuthi.borrowoke.base.BaseModel

sealed class CommonError(
    open val message: UiText
) : BaseModel() {
    object EmptyError : CommonError(UiText.Empty)

    data class NormalError(override val message: UiText, val code: Int) : CommonError(message)
}