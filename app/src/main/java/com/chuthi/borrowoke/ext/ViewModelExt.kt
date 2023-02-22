package com.chuthi.borrowoke.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ViewModel.launchViewModelScope(action: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch {
        action.invoke(this)
    }
}