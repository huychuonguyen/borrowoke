package com.chuthi.borrowoke.ext

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.other.enums.FragmentDataKey
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


// region Fragment Result
/**
 * onFragmentResultListener.
 * - Set result callback for fragment
 * based on AppcompatActivity.supportFragmentManager
 * @param requestKey the request key, type of [FragmentDataKey]
 * @param callback result with [Key] and [Bundle]
 */
fun <Key : FragmentDataKey> Fragment.onFragmentResult(
    requestKey: Key,
    callback: (Key, Bundle) -> Unit
) {
    // Re-use onFragmentResultListener extension of Activity
    (context as? AppCompatActivity)?.run {
        this.setOnFragmentResult(requestKey, callback)
    }
}

/**
 * onChildFragmentResultListener.
 * - Set result callback for fragment
 * based on Fragment.childFragmentManager.
 * @param requestKey the request key, type of [FragmentDataKey]
 * @param callback result with [Key] and [Bundle]
 */
inline fun <Key : FragmentDataKey> Fragment.onChildFragmentResult(
    requestKey: Key,
    crossinline callback: (Key, Bundle) -> Unit
) {
    childFragmentManager.onFragmentResult(
        lifecycleOwner = viewLifecycleOwner,
        requestKey = requestKey,
        callback = callback
    )
}


/**
 * setResultCallback.
 *  - Set the callback result for fragment,
 *  then listen on [onChildFragmentResult] or [onFragmentResult]
 *  @param key the request key, type of [FragmentDataKey]
 *
 */
fun <Key : FragmentDataKey> Fragment.setFragmentResultData(
    key: Key,
    vararg data: Pair<String, Any?>
) {
    if (isAdded)
        setFragmentResult(
            requestKey = key.requestKey,
            result = Bundle().apply {
                putData(*data)
            }
        )
}
// endregion