package com.chuthi.borrowoke.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.base.interfaces.LifecycleObserverFragment
import com.chuthi.borrowoke.ext.showToast
import kotlinx.coroutines.CoroutineScope

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 24/04/2023
- Project : Base Kotlin
 **************************************/
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : LifecycleObserverFragment() {

    private lateinit var _binding: VB
    protected val binding: VB
        get() = _binding

    /**
     * Fragment back pressed callback
     */
    private var fragmentBackPressedCallback: OnBackPressedCallback? = null

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

    abstract fun onArgumentsSaved(arguments: Bundle?)

    /**
     * Override this method to custom fragment back pressed.
     * Default action is raise activity's popBackStack
     */
    open fun handleFragmentBackPressed(): (() -> Unit)? = null

    /**
     * Show loading from [BaseActivity]
     */
    override fun showLoading() =
        (context as? BaseActivity<*, *>)?.showLoading() ?: Unit

    /**
     * Hide loading from [BaseActivity]
     */
    override fun hideLoading() =
        (context as? BaseActivity<*, *>)?.hideLoading() ?: Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this)
        // get data from arguments
        onArgumentsSaved(arguments)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // register backPressed
        registerFragmentBackPressed()
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
     * Register fragment back pressed
     */
    private fun registerFragmentBackPressed() {
        handleFragmentBackPressed()?.run {
            fragmentBackPressedCallback = object : OnBackPressedCallback(
                true // default to enabled
            ) {
                override fun handleOnBackPressed() {
                    this@run.invoke()
                }
            }
        }
        // register fragment back pressed
        fragmentBackPressedCallback?.let { callback ->
            (context as? BaseActivity<*, *>)?.addFragmentBackPressed(
                this,
                callback
            )
        }
    }

    private fun handleBackPressed() {
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
                } else popBackStack()
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Observe flow data from lifeCycle's [CoroutineScope] of viewLifecycleOwner
     */
    private fun observeData() {
        val viewModels = getViewModels().plus(viewModel).distinct()
        observeEvents(this, viewModels)
    }
}