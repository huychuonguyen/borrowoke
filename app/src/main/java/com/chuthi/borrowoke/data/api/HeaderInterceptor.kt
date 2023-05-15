package com.chuthi.borrowoke.data.api

import com.chuthi.borrowoke.other.DOGGO_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request().newBuilder()
        original.apply {
            val accessToken = ""
            addHeader("Authorization", "Bearer $accessToken")
            addHeader("x-api-key", DOGGO_API_KEY)
        }
        val request = original.build()
        return chain.proceed(request)
    }
}