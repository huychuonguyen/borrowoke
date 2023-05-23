package com.chuthi.borrowoke.util

import com.chuthi.borrowoke.other.DATE_FORMAT
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

inline fun <reified T> provideService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

fun getCurrentDate(): String {
    return try {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        formatter.format(currentDate)
    } catch (ex: Exception) {
        ""
    }
}