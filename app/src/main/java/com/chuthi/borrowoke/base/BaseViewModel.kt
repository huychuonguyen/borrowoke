package com.chuthi.borrowoke.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.chuthi.borrowoke.ext.launchViewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel : ViewModel() {

    private var _argumentData: Bundle? = null
    val argumentData: Bundle?
        get() = _argumentData

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    protected fun showLoading() = launchViewModelScope {
        _isLoading.emit(true)
    }

    protected fun hideLoading() = launchViewModelScope {
        _isLoading.emit(false)
    }

    fun setArguments(data: Bundle?) {
        launchViewModelScope {
            _argumentData = data
        }
    }
}