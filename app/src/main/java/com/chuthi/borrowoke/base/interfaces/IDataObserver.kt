package com.chuthi.borrowoke.base.interfaces

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.ext.getFlowData
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.HttpError
import com.chuthi.borrowoke.other.enums.asString

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

    fun showLoading()

    fun hideLoading()

    /**
     * - Use [getFlowData] or [getFlowDataLasted] extension
     * to observe FlowData on here.
     */
    fun onObserveData(): (LifecycleOwner.() -> Unit)? = null

    /**
     * Observe viewModel data on lifecycle of [view].
     * @param view the view contains [Context] and [LifecycleOwner].
     * @param viewModels list of [BaseViewModel] contains data (Flow data, Live data) to be observed.
     */
    fun <T, VM : BaseViewModel> observeData(view: T, viewModels: List<VM?>) {
        // get context and lifecycle owner
        val (context, lifeCycleOwner) = when (view) {
            is BaseActivity<*, *> -> {
                Pair(view, view)
            }

            is BaseFragment<*, *> -> {
                Pair(view.context, view.viewLifecycleOwner)
            }
            // return null if not Activity or Fragment
            else -> Pair(null, null)
        }
        // return when null
        context ?: return
        lifeCycleOwner ?: return

        // observe events
        lifeCycleOwner.run {
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