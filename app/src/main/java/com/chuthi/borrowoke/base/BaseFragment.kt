package com.chuthi.borrowoke.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.ext.hideLoading
import com.chuthi.borrowoke.ext.repeatOnLifecycle
import com.chuthi.borrowoke.ext.showLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    private lateinit var _binding: VB
    protected val binding: VB
        get() = _binding

    abstract val viewModel: VM?

    protected abstract fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    abstract fun setupUI()

    abstract fun onObserveData(): (suspend CoroutineScope.() -> Unit)?

    abstract fun onArgumentsSaved(arguments: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get data from arguments
        onArgumentsSaved(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = setViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // raise setup ui
        setupUI()
        // raise observe data
        observeData()
    }

    /**
     * Observe flow data from lifeCycle's [CoroutineScope] of viewLifecycleOwner
     */
    private fun observeData() {
        repeatOnLifecycle {
            // observer loading
            launch {
                viewModel?.isLoading?.collectLatest { isLoading ->
                    if (isLoading) showLoading()
                    else hideLoading()
                }
            }
            // raise observe data on coroutine
            onObserveData()?.invoke(this)
        }
    }
}