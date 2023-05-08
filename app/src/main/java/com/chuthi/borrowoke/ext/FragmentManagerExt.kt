package com.chuthi.borrowoke.ext

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.other.enums.FragmentDataKey

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
 * @param requestKey the request key, type of [FragmentDataKey]
 * @param callback result with [Keys] and [Bundle]
 */
inline fun <Keys : FragmentDataKey> FragmentManager.onFragmentResult(
    lifecycleOwner: LifecycleOwner,
    requestKey: Keys,
    crossinline callback: (Keys, Bundle) -> Unit
) {
    setFragmentResultListener(
        requestKey.requestKey,
        lifecycleOwner
    ) { _, bundle ->
        callback.invoke(requestKey, bundle)
    }
}
