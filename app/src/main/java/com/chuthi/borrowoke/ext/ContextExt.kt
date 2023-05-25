package com.chuthi.borrowoke.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Copy a text to clipboard
 */
fun Context.copyText(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText("Copied", text)
    clipboard?.setPrimaryClip(clip)
}