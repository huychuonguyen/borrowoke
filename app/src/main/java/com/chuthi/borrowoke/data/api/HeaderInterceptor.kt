package com.chuthi.borrowoke.data.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request().newBuilder()
        original.apply {
            val accessToken = ""
            addHeader("Authorization", "Bearer $accessToken")
        }
        val request = original.build()
        return chain.proceed(request)
    }
}