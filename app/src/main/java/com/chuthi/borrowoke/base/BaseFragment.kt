package com.chuthi.borrowoke.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.ext.getFlowData
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.repeatOnLifecycle
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.enums.HttpError
import com.chuthi.borrowoke.other.enums.asString
import kotlinx.coroutines.CoroutineScope

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 24/04/2023
- Project : Base Kotlin
 **************************************/
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

    /**
     * - Attach other viewModels if activity have more one viewModel
     * besides the main [viewModel] variable.
     * - Notice: Do not add the [viewModel] variable into this list.
     */
    open fun getViewModels(): List<BaseViewModel> = emptyList()

    protected abstract fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): VB

    abstract fun setupUI()

    /**
     * - Use [getFlowData] or [getFlowDataLasted] extension
     * to observe FlowData on here.
     */
    open fun observeFlowData(): (CoroutineScope.() -> Unit)? = null

    /**
     * Use [getLiveData] extension
     * to observe LiveData on here.
     */
    open fun observeLiveData(): (LifecycleOwner.() -> Unit)? = null

    abstract fun onArgumentsSaved(arguments: Bundle?)

    /**
     * Override this method to custom fragment back pressed.
     * Default action is raise activity's popBackStack
     */
    open fun handleFragmentBackPressed() {
        try {
            findNavController().run {
                // get start destination id
                val startDesId = graph.startDestinationId
                // true: pop success
                // false: pop fail and current destination is start destination
                val currentDest = findNavController().currentDestination

                // false -> finish Activity
                if (currentDest?.id == startDesId) {
                    showToast("startDes: ${this@BaseFragment::class.java.simpleName}")
                    //(context as? BaseActivity<*, *>)?.finish()
                } else popBackStack()
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Show loading from [BaseActivity]
     */
    open fun showLoading() =
        (context as? BaseActivity<*, *>)?.showLoading() ?: Unit

    /**
     * Hide loading from [BaseActivity]
     */
    open fun hideLoading() =
        (context as? BaseActivity<*, *>)?.hideLoading() ?: Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get data from arguments
        onArgumentsSaved(arguments)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // register fragment back pressed
        (context as? BaseActivity<*, *>)?.addFragmentBackPressed(
            this,
            fragmentBackPressedCallback
        )
    }

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
        val viewModels = getViewModels().plus(viewModel).distinct()
        repeatOnLifecycle {
            //  observe loading, error on each viewModel
            viewModels.forEach { viewModel ->
                viewModel?.run {
                    // observe loading
                    getFlowDataLasted(isLoading) { isLoading ->
                        if (isLoading) showLoading()
                        else hideLoading()
                    }
                    // observe error
                    getFlowData(error) { commonError ->
                        val errorMess = commonError.message.asString(context)
                        when (commonError) {
                            is HttpError.Unauthorized401 -> {
                                // open authentication activity
                                //openActivity(targetActivity = AuthenticationActivity::class.java)
                            }

                            else -> showToast(errorMess)
                        }
                    }
                }
            }
            // raise observe FlowData on coroutine
            observeFlowData()?.invoke(this)
        }
        // raise observe LiveData
        observeLiveData()?.invoke(viewLifecycleOwner)
    }
}