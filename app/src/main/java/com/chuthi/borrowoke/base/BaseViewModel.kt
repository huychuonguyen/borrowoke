package com.chuthi.borrowoke.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.CommonError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel : ViewModel() {

    private var _argumentData: Bundle? = null
    val argumentData: Bundle?
        get() = _argumentData

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    protected val commonError = MutableSharedFlow<CommonError>()
    val error = commonError.asSharedFlow()


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