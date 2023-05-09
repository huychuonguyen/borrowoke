package com.chuthi.borrowoke.ext

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.other.enums.FragmentResultKey

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 08/05/2024
- Project : Base Kotlin
 **************************************/

/*FragmentManager Extension*/


/**
 * setResultListener.
 * - Set result callback for fragment
 * @param requestKey the request key, type of [FragmentResultKey]
 * @param callback result with [Key] and [Bundle]
 */
inline fun <Key : FragmentResultKey> FragmentManager.onFragmentResult(
    lifecycleOwner: LifecycleOwner,
    requestKey: Key,
    crossinline callback: (Key, Bundle) -> Unit
) = setFragmentResultListener(
    requestKey.requestKey,
    lifecycleOwner
) { key, bundle ->
    Log.i("onFragmentResult", "Key: $key | Data: $bundle")
    callback.invoke(requestKey, bundle)
}


/**
 * Replace fragment
 */
fun FragmentManager.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    isAddToBackStack: Boolean = false
) = beginTransaction()
    .replace(containerId, fragment, tag)
    .apply {
        if (isAddToBackStack)
            addToBackStack(tag ?: fragment::class.java.simpleName)
    }.commit()


