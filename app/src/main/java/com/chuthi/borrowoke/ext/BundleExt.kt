package com.chuthi.borrowoke.ext

import android.os.Build
import android.os.Bundle
import android.os.Parcelable


/**
 * put parcelize data
 */
fun <T : Parcelable> Bundle.putData(
    key: String,
    value: T
): Bundle = apply {
    putParcelable(
        key, value
    )
}

/**
 * get Parcelize data
 */
private inline fun <reified T : Parcelable> Bundle.getData(
    key: String
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    getParcelable(key, T::class.java)
else
    getParcelable(key) as? T

