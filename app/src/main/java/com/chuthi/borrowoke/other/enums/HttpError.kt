package com.chuthi.borrowoke.other.enums

sealed class HttpError(val code: Int, message: UiText) : CommonError(message) {

    object Unauthorized401 : HttpError(CODE_401, UiText.DynamicString("Unauthorized 401"))
    object Unauthorized403 : HttpError(CODE_403, UiText.DynamicString("Unauthorized 403"))
    object NotFound : HttpError(CODE_404, UiText.DynamicString("Not Found Error"))

    companion object {
        // define error code
        private const val CODE_401 = 401
        private const val CODE_403 = 403
        private const val CODE_404 = 404
    }
}