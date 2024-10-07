package com.chuthi.borrowoke.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 25/04/2023
- Project : Base Kotlin
 **************************************/
inline fun <T> LifecycleOwner.getLiveData(
    liveData: LiveData<T>,
    crossinline data: (T) -> Unit
) {
    // If this is fragment,
    // it means this extension use lifecycleOwner of the fragment
    // instead of viewLifecycleOwner.
    val lifecycleOwner = if (this is Fragment) viewLifecycleOwner else this

    liveData.observe(lifecycleOwner) {
        it ?: return@observe
        data.invoke(it)
    }
}

inline fun <T> LifecycleOwner.getFlowData(
    flowData: (Flow<T>)?,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline data: suspend (T) -> Unit
) = repeatOnLifeCycle(state) {
    flowData?.collect {
        data.invoke(it)
    }
}

inline fun <T> LifecycleOwner.getFlowDataLasted(
    flowData: (Flow<T>)?,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline data: suspend (T) -> Unit
) = repeatOnLifeCycle(state) {
    flowData?.collectLatest {
        data.invoke(it)
    }
}

/**
 * repeatOnLifecycle extension pn [LifecycleOwner]
 *
 */
inline fun LifecycleOwner.repeatOnLifeCycle(
    state: Lifecycle.State,
    crossinline action: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(state) {
        action.invoke(this)
    }
}