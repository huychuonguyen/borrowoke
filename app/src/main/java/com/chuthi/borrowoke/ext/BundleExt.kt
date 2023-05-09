package com.chuthi.borrowoke.ext

import android.os.Build
import android.os.Bundle
import android.os.Parcelable


/**
 * put parcelize data
 */
fun <T : Any> Bundle.putData(
    vararg pairs: Pair<String, T?>
) = apply {
    for ((key, value) in pairs) {
        putParcelable(
            key,
            value?.toParcelableData()
        )
    }
}

/**
 * get Parcelize data.
 */
@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getData(
    key: String
): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, T::class.java)
    else
        getParcelable(key) as? T
}




