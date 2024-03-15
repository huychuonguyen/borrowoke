package com.chuthi.borrowoke.ext

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.chuthi.borrowoke.other.enums.FragmentResultKey
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
 * setFragmentResultListener.
 * - Set result callback for fragment from Activity.
 * @param requestKey the request key, type of [FragmentResultKey]
 * @param callback result with [Key] and [Bundle]
 */
inline fun <Key : FragmentResultKey> AppCompatActivity.onFragmentResult(
    requestKey: Key,
    crossinline callback: (Key, Bundle) -> Unit
) {
    supportFragmentManager.onFragmentResult(
        lifecycleOwner = this,
        requestKey = requestKey,
        callback = callback
    )
}

fun AppCompatActivity.runOnCoroutine(action: CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        action.invoke(this)
    }
}

fun AppCompatActivity.openBrowser(url: String, onError: (String) -> Unit = {}) {
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } catch (error: ActivityNotFoundException) {
        onError.invoke(error.message.toString())
    }
}

fun AppCompatActivity.backToSystemHome() = Intent(Intent.ACTION_MAIN).let { startMain ->
    startMain.addCategory(Intent.CATEGORY_HOME)
    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(startMain)
}

fun AppCompatActivity.setStatusBarColor(@ColorInt color: Int) {
    window?.apply {
        statusBarColor = color
    }
}

fun AppCompatActivity.setNavigationBarColor(@ColorInt color: Int) {
    window?.apply {
        navigationBarColor = color
    }
}

fun AppCompatActivity.setDecorFitsSystemWindows(on: Boolean) {
    window?.apply {
        WindowCompat.setDecorFitsSystemWindows(
            this, on
        )
    }
}

fun AppCompatActivity.getSystemNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0

}
