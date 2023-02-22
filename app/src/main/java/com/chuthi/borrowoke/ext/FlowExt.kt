package com.chuthi.borrowoke.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.observeFlow(
    lifecycleOwner: LifecycleOwner,
    crossinline action: suspend (T) -> Unit
) {
    lifecycleOwner.run {
        lifecycleScope.launch {
            flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collectLatest {
                action.invoke(it)
            }
        }
    }
}