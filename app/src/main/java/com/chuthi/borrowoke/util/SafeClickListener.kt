package com.chuthi.borrowoke.util

import android.os.SystemClock
import android.view.View

/**
 * Custom OnClickListener with delay time.
 */
class SafeClickListener(
    private var defaultInterval: Long = 500L,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        // raise click callback
        onSafeCLick(v)
    }
}