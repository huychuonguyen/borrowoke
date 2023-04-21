package com.chuthi.borrowoke.ext

import android.view.View
import androidx.core.content.ContextCompat
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.other.DEFAULT_CLICK_INTERVAL
import com.chuthi.borrowoke.util.SafeClickListener
import com.google.android.material.snackbar.Snackbar

/**
 * Register safe click listener with delay time
 * @param defaultInterval delay time
 */
fun View.onSafeClick(
    defaultInterval: Long = DEFAULT_CLICK_INTERVAL,
    onSafeClick: (View) -> Unit
) {
    // register on click with delay
    val safeClickListener = SafeClickListener(defaultInterval = defaultInterval) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
    // set background effect
    background ?: run {
        background = ContextCompat.getDrawable(context, R.drawable.bg_click)
    }
}

fun View.showSnackBar(
    message: String?,
    isLengthLong: Boolean = false,
    actionText: String? = null,
    action: () -> Unit = {}
) {
    message ?: return
    Snackbar
        .make(
            this,
            message,
            if (isLengthLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
        ).apply {
            actionText ?: return@apply
            setAction(actionText) {
                action.invoke()
            }
            setActionTextColor(ContextCompat.getColor(context, R.color.yellow))
        }
        .show()
}
