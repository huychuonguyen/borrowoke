package com.chuthi.borrowoke.util

import com.chuthi.borrowoke.base.BaseModel

sealed class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null
) : BaseModel() {

    class Success<T>(data: T) : ApiResponse<T>(data)

    open class Error<T>(
        message: String,
        val errorCode: Int = DEFAULT_ERROR_CODE
    ) : ApiResponse<T>(null, message)

    class Nothing<T> : ApiResponse<T>()

    class UnKnownError<T> : Error<T>(message = UNKNOWN_ERROR)

    class NetworkError<T> : Error<T>(message = NETWORK_ERROR)

    companion object {
        private const val DEFAULT_ERROR_CODE = 0
        private const val UNKNOWN_ERROR = "Unknown Error"
        private const val NETWORK_ERROR = "Network Error"
    }
}
