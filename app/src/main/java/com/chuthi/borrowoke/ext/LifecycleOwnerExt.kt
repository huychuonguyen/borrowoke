package com.chuthi.borrowoke.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 25/04/2023
- Project : Base Kotlin
 **************************************/
inline fun <T> LifecycleOwner.getLiveData(
    liveData: LiveData<T>,
    crossinline result: (T) -> Unit
) {
    // return if this is fragment,
    // it means this extension use lifecycleOwner of the fragment
    // instead of viewLifecycleOwner.
    if (this is Fragment) return

    liveData.observe(this) { data ->
        data ?: return@observe
        result.invoke(data)
    }
}
