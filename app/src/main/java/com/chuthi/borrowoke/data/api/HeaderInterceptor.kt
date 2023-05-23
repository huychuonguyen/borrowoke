package com.chuthi.borrowoke.data.api

import com.chuthi.borrowoke.other.NETWORK_ERROR_CODE
import com.chuthi.borrowoke.other.NETWORK_ERROR_MESS
import com.chuthi.borrowoke.other.OPENAI_API_KEY
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request().newBuilder()
        original.apply {
            //addHeader("Authorization", "Bearer $accessToken")
            //addHeader("x-api-key", DOGGO_API_KEY)
            addHeader("Authorization", "Bearer $OPENAI_API_KEY")
        }
        val request = original.build()

        return try {
            chain.proceed(request)
        } catch (ex: Exception) {
            val mess = NETWORK_ERROR_MESS
            return Response.Builder()
                .request(request)
                .code(NETWORK_ERROR_CODE)
                .protocol(Protocol.HTTP_2)
                .body(mess.toResponseBody(null))
                .message(mess).build()
        }
    }
}