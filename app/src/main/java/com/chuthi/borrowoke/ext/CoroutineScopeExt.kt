package com.chuthi.borrowoke.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <T> CoroutineScope.getFlowData(
    flowData: (Flow<T>)?,
    crossinline data: (T) -> Unit
) {
    launch {
        flowData?.collect {
            data.invoke(it)
        }
    }
}

inline fun <T> CoroutineScope.getFlowDataLasted(
    flowData: (Flow<T>)?,
    crossinline data: (T) -> Unit
) {
    launch {
        flowData?.collectLatest {
            data.invoke(it)
        }
    }
}