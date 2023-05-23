package com.chuthi.borrowoke.data.api

import com.chuthi.borrowoke.BaseApp
import com.chuthi.borrowoke.other.NETWORK_ERROR_CODE
import com.chuthi.borrowoke.other.NETWORK_ERROR_MESS
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ConnectivityInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!BaseApp.instance.isNetworkAvailable()) {
            val mess = NETWORK_ERROR_MESS
            // return network error code and catch on [ResponseExt]
            return Response.Builder()
                .request(request)
                .code(NETWORK_ERROR_CODE)
                .protocol(Protocol.HTTP_2)
                .body(mess.toResponseBody(null))
                .message(mess).build()
        }
        return chain.proceed(chain.request().newBuilder().build())
    }
}