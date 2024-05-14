package com.chuthi.borrowoke.util

import android.util.Log

interface MyLogManager {
    fun d(s: String, tag: String? = null)
    fun i(s: String, tag: String? = null)
    fun w(s: String, tag: String? = null)
}

class MyLogManagerIpl : MyLogManager {

    companion object {
        private const val TAG_D = "huychuonguyen:d"
        private const val TAG_I = "huychuonguyen:i"
        private const val TAG_W = "huychuonguyen:w"
    }

    override fun d(s: String, tag: String?) {
        val subTag = if (tag.isNullOrEmpty()) "" else "_$tag"
        Log.d("${TAG_D}$subTag", s)
    }

    override fun i(s: String, tag: String?) {
        val subTag = if (tag.isNullOrEmpty()) "" else "_$tag"
        Log.i("${TAG_I}$subTag", s)
    }

    override fun w(s: String, tag: String?) {
        val subTag = if (tag.isNullOrEmpty()) "" else "_$tag"
        Log.w("${TAG_W}$subTag", s)
    }
}
