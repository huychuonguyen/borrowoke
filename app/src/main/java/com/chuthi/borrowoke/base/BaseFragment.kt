package com.chuthi.borrowoke.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.ext.getFlowData
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.hideLoading
import com.chuthi.borrowoke.ext.popBackStack
import com.chuthi.borrowoke.ext.repeatOnLifecycle
import com.chuthi.borrowoke.ext.showLoading
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.enums.HttpError
import com.chuthi.borrowoke.other.enums.asString
import kotlinx.coroutines.CoroutineScope

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    private lateinit var _binding: VB
    protected val binding: VB
        get() = _binding

    /**
     * Fragment back pressed callback
     */
    private val fragmentBackPressedCallback = object : OnBackPressedCallback(
        true // default to enabled
    ) {
        override fun handleOnBackPressed() {
            handleFragmentBackPressed()
        }
    }

    abstract val viewModel: VM?

    protected abstract fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): VB

    abstract fun setupUI()

    /**
     * - Use [getFlowData] or [getFlowDataLasted] extension
     * to observe FlowData on here.
     */
    open fun observeFlowData(): (suspend CoroutineScope.() -> Unit)? = null

    /**
     * Use [getLiveData] extension
     * to observe LiveData on here.
     */
    open fun observeLiveData(): (() -> Unit)? = null

    abstract fun onArgumentsSaved(arguments: Bundle?)

    /**
     * Override this method to custom fragment back pressed.
     * Default action is raise activity's popBackStack
     */
    open fun handleFragmentBackPressed() {
        (context as? BaseActivity<*, *>)?.popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get data from arguments
        onArgumentsSaved(arguments)
    }

    /* override fun onAttach(context: Context) {
         super.onAttach(context)
         // register fragment back pressed
          (context as? BaseActivity<*, *>)?.addFragmentBackPressed(
              this,
              fragmentBackPressedCallback
          )
     }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // raise setup ui
        setupUI()
        // raise observe data
        observeData()
    }

    override fun onDestroyView() {
        // hide loading on view destroyed
        hideLoading()
        super.onDestroyView()
    }

    /**
     * Observe flow data from lifeCycle's [CoroutineScope] of viewLifecycleOwner
     */
    private fun observeData() {
        // observe Live data
        observeLiveData()?.invoke()
        // observe Flow data
        repeatOnLifecycle {
            // observer loading
            getFlowDataLasted(viewModel?.isLoading) { isLoading ->
                if (isLoading) showLoading()
                else hideLoading()
            }
            // observe error
            getFlowData(viewModel?.error) { commonError ->
                context?.run {
                    val errorMess = commonError.message.asString(this)

                    when (commonError) {
                        is HttpError.Unauthorized401 -> {
                            // open authentication activity
                            //openActivity(targetActivity = AuthenticationActivity::class.java)
                        }

                        else -> showToast(errorMess)
                    }
                }
            }
            // raise observe data on coroutine
            observeFlowData()?.invoke(this)
        }
    }
}