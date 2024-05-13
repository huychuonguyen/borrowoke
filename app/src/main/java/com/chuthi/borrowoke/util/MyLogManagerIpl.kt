package com.chuthi.borrowoke.util

import android.util.Log

class MyLogManagerIpl : MyLogManager {

    override fun d(s: String, tag: String) {
        Log.d(tag, s)
    }

    override fun i(s: String, tag: String) {
        Log.i(tag, s)
    }

    override fun w(s: String, tag: String) {
        Log.w(tag, s)
    }
}

interface MyLogManager {
    companion object {
        private const val DEFAULT_LOG_TAG_D = "huychuonguyen_d"
        private const val DEFAULT_LOG_TAG_I = "huychuonguyen_i"
        private const val DEFAULT_LOG_TAG_W = "huychuonguyen_w"
    }

    fun d(s: String, tag: String = DEFAULT_LOG_TAG_D)

    fun i(s: String, tag: String = DEFAULT_LOG_TAG_I)

    fun w(s: String, tag: String = DEFAULT_LOG_TAG_W)
}
