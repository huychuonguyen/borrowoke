package com.chuthi.borrowoke.ext

import android.view.View
import com.chuthi.borrowoke.other.DEFAULT_CLICK_INTERVAL
import com.chuthi.borrowoke.util.SafeClickListener

/**
 * Register safe click listener with delay time
 * @param defaultInterval delay time
 */
fun View.onSafeClick(
    defaultInterval: Long = DEFAULT_CLICK_INTERVAL,
    onSafeClick: (View) -> Unit
) {
    val safeClickListener = SafeClickListener(defaultInterval = defaultInterval) {
        onSafeClick(it)
    }
    // register safe click
    setOnClickListener(safeClickListener)
}