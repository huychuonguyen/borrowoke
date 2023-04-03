package com.chuthi.borrowoke.util

import retrofit2.Retrofit

inline fun <reified T> provideService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}
