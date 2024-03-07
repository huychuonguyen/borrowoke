package com.chuthi.borrowoke.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun ViewModel.launchWithCoroutineContext(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(coroutineContext) {
    action.invoke(this)
}
