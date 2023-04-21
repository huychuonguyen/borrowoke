package com.chuthi.borrowoke.ext

import com.chuthi.borrowoke.util.ApiResponse
import retrofit2.Response

/**
 * [Response] extension to call api and catch exceptions.
 */
suspend fun <T> Response<T>.apiCall(
    onSuccess: suspend (ApiResponse.Success<T>) -> Unit,
    onError: suspend (ApiResponse.Error<T>) -> Unit,
    /**
     * Action raised when api call done. (success/fail/...),
     * usually used to hide loading dialog
     */
    onFinished: suspend () -> Unit = {}
) = when (isSuccessful) {
    // success
    true -> {
        val data = body()
        data ?: run {
            // raise error
            onError.invoke(
                ApiResponse.Error(
                    message = "Data not found",
                    errorCode = 0
                )
            )
            // raise finish
            onFinished.invoke()
            return
        }
        onSuccess.invoke(ApiResponse.Success(data = data))
        // raise finish
        onFinished.invoke()
    }
    // error
    else -> {
        val errorCode = code()
        onError.invoke(
            ApiResponse.Error(
                message = "Response exception",
                errorCode = errorCode
            )
        )
        // raise finish
        onFinished.invoke()
    }
}
