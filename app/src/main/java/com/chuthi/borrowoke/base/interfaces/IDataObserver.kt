package com.chuthi.borrowoke.base.interfaces

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.ext.getFlowData
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.repeatOnLifecycle
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.HttpError
import com.chuthi.borrowoke.other.enums.asString
import kotlinx.coroutines.CoroutineScope

interface IDataObserver {

    fun showLoading()

    fun hideLoading()

    /**
     * - Use [getFlowData] or [getFlowDataLasted] extension
     * to observe FlowData on here.
     */
    fun observeFlowData(): (CoroutineScope.() -> Unit)? = null

    /**
     * Use [getLiveData] extension
     * to observe LiveData on here.
     */
    fun observeLiveData(): (LifecycleOwner.() -> Unit)? = null

    fun observeEvents(
        activity: BaseActivity<*, *>,
        viewModels: List<BaseViewModel?>
    ) {
        activity.run {
            repeatOnLifecycle {
                handleEvents(activity, viewModels, this)
            }
            // raise observe LiveData
            observeLiveData()?.invoke(this)
        }
    }

    fun observeEvents(
        fragment: BaseFragment<*, *>,
        viewModels: List<BaseViewModel?>
    ) {
        fragment.run {
            repeatOnLifecycle {
                context?.let { activity ->
                    handleEvents(activity, viewModels, this)
                }
            }
            // raise observe LiveData
            observeLiveData()?.invoke(viewLifecycleOwner)
        }
    }

    private fun handleEvents(
        context: Context,
        viewModels: List<BaseViewModel?>,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.run {
            viewModels.forEach { viewModel ->
                viewModel?.run {
                    // observe loading
                    getFlowDataLasted(isLoading) { isLoading ->
                        if (isLoading) showLoading()
                        else hideLoading()
                    }
                    // observe error
                    getFlowData(error) { commonError ->
                        handleError(context, commonError)
                    }
                }
            }
            // raise observe FlowData on coroutine
            observeFlowData()?.invoke(this)
        }
    }

    private fun handleError(context: Context?, error: CommonError) {
        (context as? AppCompatActivity)?.run {
            val errorMess = error.message.asString(this)
            when (error) {
                HttpError.Unauthorized401 -> {

                }

                else -> {
                    showToast(errorMess)
                }
            }
        }
    }
}