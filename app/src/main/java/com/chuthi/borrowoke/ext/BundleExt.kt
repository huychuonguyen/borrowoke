package com.chuthi.borrowoke.ext

import android.os.Build
import android.os.Bundle
import com.chuthi.borrowoke.data.model.ParcelizeData


/**
 * convert [T] to [ParcelizeData] and put it to bundle.
 */
fun <T> Bundle.putData(
    vararg pairs: Pair<String, T?>
) = run {
    for ((key, value) in pairs) {
        putParcelable(
            key,
            value?.toParcelableData()
        )
    }
}

/**
 * get [ParcelizeData] data and convert to [T].
 */
@Suppress("DEPRECATION")
inline fun <reified T> Bundle.getData(
    key: String
): T? {
    // get ParcelizeData
    val parcelizeData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, ParcelizeData::class.java)
    else
        getParcelable(key) as? ParcelizeData<T>
    // then return raw value as T
    return parcelizeData?.value as? T
}





