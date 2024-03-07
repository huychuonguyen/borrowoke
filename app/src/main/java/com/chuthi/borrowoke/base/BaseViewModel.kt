package com.chuthi.borrowoke.base

import androidx.lifecycle.ViewModel
import com.chuthi.borrowoke.BaseApp
import com.chuthi.borrowoke.ext.launchWithCoroutineContext
import com.chuthi.borrowoke.other.enums.CommonError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 24/04/2023
- Project : Base Kotlin
 **************************************/
open class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /**
     * User [MutableSharedFlow] as onetime callback
     * tp observe error
     */
    protected val commonError = MutableSharedFlow<CommonError>()
    val error = commonError.asSharedFlow()


    protected fun showLoading() = launchViewModelScope {
        _isLoading.emit(true)
    }

    protected fun hideLoading() = launchViewModelScope {
        _isLoading.emit(false)
    }


    protected fun launchViewModelScope(
        coroutineContext: CoroutineContext = EmptyCoroutineContext,
        action: suspend CoroutineScope.() -> Unit
    ) = launchWithCoroutineContext(coroutineContext, action)


    protected fun sendError(error: CommonError) = launchWithCoroutineContext {
        commonError.emit(error)
    }

    /**
     * Await suspend fun to success
     */
    protected suspend fun <T> awaitSuspend(block: suspend () -> T) =
        withContext(Dispatchers.Default) {
            block()
        }

    /**
     * Check network connection
     */
    protected fun isNetworkAvailable() = BaseApp.instance.isNetworkAvailable()

}