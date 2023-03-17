package com.chuthi.borrowoke.ext

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Show Toast
 */
fun AppCompatActivity.showToast(
    mess: String? = null,
    isLengthLong: Boolean = false
) {
    if (mess.isNullOrEmpty()) return
    Toast.makeText(
        this,
        mess,
        if (isLengthLong) Toast.LENGTH_LONG
        else Toast.LENGTH_SHORT
    ).show()
}

/**
 * repeatOnLifecycle extension
 * based on lifeCycle's [CoroutineScope] of activity
 */
fun AppCompatActivity.repeatOnLifecycle(
    action: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.invoke(this)
        }
    }
}

fun AppCompatActivity.popBackStack() = supportFragmentManager
    .popBackStack()