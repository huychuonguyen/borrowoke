package com.chuthi.borrowoke.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chuthi.borrowoke.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Show Toast
 */
fun Fragment.showToast(mess: String? = null, isLengthLong: Boolean = false) =
    (context as? BaseActivity<*, *>)?.showToast(mess, isLengthLong)

/**
 * repeatOnLifecycle extension
 * based on lifeCycle's [CoroutineScope] of viewLifecycleOwner
 */
fun Fragment.repeatOnLifecycle(
    action: CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.invoke(this)
        }
    }
}
