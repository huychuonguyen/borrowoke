package com.chuthi.borrowoke.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chuthi.borrowoke.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Show loading from [BaseActivity]
 */
fun Fragment.showLoading() = (context as? BaseActivity<*, *>)?.showLoading()

/**
 * Hide loading from [BaseActivity]
 */
fun Fragment.hideLoading() = (context as? BaseActivity<*, *>)?.hideLoading()

/**
 * Show Toast
 */
fun Fragment.showToast(mess: String, isLengthLong: Boolean = false) =
    (context as? BaseActivity<*, *>)?.showToast(mess, isLengthLong)

/**
 * repeatOnLifecycle extension
 * based on lifeCycle's [CoroutineScope] of viewLifecycleOwner
 */
fun Fragment.repeatOnLifecycle(
    action: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.invoke(this)
        }
    }
}

inline fun <T> Fragment.getLiveData(
    liveData: LiveData<T>,
    crossinline result: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner) { data ->
        data ?: return@observe
        result.invoke(data)
    }
}
