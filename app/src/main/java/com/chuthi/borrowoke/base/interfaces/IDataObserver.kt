package com.chuthi.borrowoke.base.interfaces

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.ext.getFlowData
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.HttpError
import com.chuthi.borrowoke.other.enums.asString
import com.chuthi.borrowoke.util.MyLogManager

/**
 * - The interface with common data handling and ui event.
 * - Include abstract methods:
 *      - showLoading
 *      - hideLoading
 *      - observeEvents
 *      - observeFlowData
 *      - observeLiveData
 *      - handleError
 *      - ...
 */
interface IDataObserver {

    val log: MyLogManager

    fun showLoading()

    fun hideLoading()

    /**
     * - Use [getFlowData], [getFlowDataLasted] or [getLiveData] extension
     * to observe data on here.
     */
    fun onObserveData(): (LifecycleOwner.() -> Unit)? = null

    /**
     * Observe viewModel data on [lifecycleOwner].
     * @param viewModels list of [BaseViewModel] contains data (Flow data, Live data) to be observed.
     */
    fun <VM : BaseViewModel> observeData(
        context: Context?,
        lifecycleOwner: LifecycleOwner?,
        viewModels: List<VM?>
    ) {
        // return when null
        context ?: return
        // observe events
        lifecycleOwner?.run {
            // handle FlowData
            handleEvents(context, viewModels, this)
            // raise observe FlowData/LiveData on [LifecycleOwner]
            onObserveData()?.invoke(this)
        }
    }

    /**
     * Handle FlowData on lifecycle with coroutine.
     */
    private fun handleEvents(
        context: Context,
        viewModels: List<BaseViewModel?>,
        lifecycleOwner: LifecycleOwner
    ) {
        lifecycleOwner.run {
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
        }
    }

    /**
     * Handle error.
     */
    private fun handleError(context: Context?, error: CommonError) {
        (context as? AppCompatActivity)?.run {
            val errorMess = error.message.asString(this)
            when (error) {
                is HttpError.Unauthorized401 -> {

                }

                else -> showToast(errorMess)
            }
        }
    }
}