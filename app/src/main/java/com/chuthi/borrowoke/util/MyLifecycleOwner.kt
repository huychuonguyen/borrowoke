package com.chuthi.borrowoke.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.ext.getFlowData
import com.chuthi.borrowoke.ext.getFlowDataLasted
import kotlinx.coroutines.flow.Flow

class MyLifecycleOwner(override val lifecycle: Lifecycle) : LifecycleOwner {

    inline infix fun <T> Pair<Flow<T>, Lifecycle.State>.bindTo(
        crossinline action: suspend (T) -> Unit
    ) {
        getFlowData(first, second) {
            action.invoke(it)
        }
    }

    inline infix fun <T> Flow<T>.bindTo(
        crossinline action: suspend (T) -> Unit
    ) {
        getFlowData(this) {
            action.invoke(it)
        }
    }

    inline infix fun <T> Flow<T>.bindToLatest(
        crossinline action: suspend (T) -> Unit
    ) {
        getFlowDataLasted(this) {
            action.invoke(it)
        }
    }

    infix fun <T> Flow<T>.with(
        state: Lifecycle.State
    ) = this to state

}