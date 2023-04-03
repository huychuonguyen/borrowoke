package com.chuthi.borrowoke.ext

import com.chuthi.borrowoke.util.ApiResponse
import retrofit2.Response

/**
 * [Response] extension to call api and catch exceptions.
 */
suspend fun <T> Response<T>.apiCall(
    onSuccess: suspend (ApiResponse.Success<T>) -> Unit,
    onException: suspend (ApiResponse.Error<T>) -> Unit
) = when (isSuccessful) {
    // success
    true -> {
        val data = body()
        data ?: run {
            // raise error
            onException.invoke(
                ApiResponse.Error(
                    message = "Data not found",
                    errorCode = 0
                )
            )
            return
        }
        onSuccess.invoke(ApiResponse.Success(data = data))
        onException.invoke(
            ApiResponse.Error(
                message = "Response exception",
                errorCode = 1995
            )
        )
    }
    // error
    else -> {
        val errorCode = code()
        onException.invoke(
            ApiResponse.Error(
                message = "Response exception",
                errorCode = errorCode
            )
        )
    }
}
