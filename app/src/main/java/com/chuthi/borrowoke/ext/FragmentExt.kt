package com.chuthi.borrowoke.ext

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.other.enums.FragmentResultKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Show Toast
 */
fun Fragment.showToast(mess: String? = null, isLengthLong: Boolean = false) =
    (context as? BaseActivity<*, *>)?.showToast(mess, isLengthLong)


// region Fragment Result
/**
 * onFragmentResultListener.
 * - Set result callback for fragment
 * based on AppCompatActivity.supportFragmentManager
 * @param requestKey the request key, type of [FragmentResultKey]
 * @param callback result with [Key] and [Bundle]
 */
fun <Key : FragmentResultKey> Fragment.onFragmentResult(
    requestKey: Key,
    callback: (Key, Bundle) -> Unit
) {
    // Re-use onFragmentResultListener extension of Activity
    (context as? AppCompatActivity)?.run {
        this.onFragmentResult(requestKey, callback)
    }
}

/**
 * Retrieve result between fragments on NavHostFragment
 */
fun <Key : FragmentResultKey> Fragment.onParentFragmentResult(
    requestKey: Key,
    callback: (Key, Bundle) -> Unit
) {
    // Re-use onFragmentResultListener extension of Activity
    parentFragmentManager.onFragmentResult(
        lifecycleOwner = viewLifecycleOwner,
        requestKey = requestKey,
        callback = callback
    )
}

/**
 * onChildFragmentResultListener.
 * - Set result callback for fragment
 * based on Fragment.childFragmentManager.
 * @param requestKey the request key, type of [FragmentResultKey]
 * @param callback result with [Key] and [Bundle]
 */
inline fun <Key : FragmentResultKey> Fragment.onChildFragmentResult(
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
 *  @param key the request key, type of [FragmentResultKey]
 *
 */
fun <Key : FragmentResultKey> Fragment.setFragmentResultData(
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