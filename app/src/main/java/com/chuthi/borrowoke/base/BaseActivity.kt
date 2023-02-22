package com.chuthi.borrowoke.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.ext.repeatOnLifecycle
import com.chuthi.borrowoke.ui.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : LifecycleObserverActivity() {

    private lateinit var _binding: VB
    protected val binding: VB
        get() = _binding

    /**
     * Loading dialog
     */
    private var _loadingDialog: LoadingDialog? = null

    /**
     * Flag to check is [LoadingDialog] showing
     */
    private var _isShowLoading = false

    protected abstract val viewModel: VM?

    protected abstract fun setViewBinding(inflater: LayoutInflater): VB

    abstract fun setupUI()

    abstract fun onObserveData(): (suspend CoroutineScope.() -> Unit)?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this)
        _binding = setViewBinding(layoutInflater)
        // set layout id
        setContentView(binding.root)
        // init dialogs
        initDialogs()
        // setup ui
        setupUI()
        // observe data
        observeData()
    }

    private fun initDialogs() {
        _loadingDialog = LoadingDialog()
    }

    fun showLoading() {
        val dialogAlreadyShown =
            supportFragmentManager.findFragmentByTag(LoadingDialog.TAG)
                    as? LoadingDialog
        // if loading dialog is already shown -> not show again
        // else -> show loading dialog
        dialogAlreadyShown ?: run {
            _loadingDialog?.let { dialog ->
                if (!_isShowLoading)
                    supportFragmentManager
                        .beginTransaction()
                        .add(dialog, LoadingDialog.TAG)
                        .runOnCommit {
                            // reset loading flag
                            _isShowLoading = false
                        }
                        .commitAllowingStateLoss()
                        .also {
                            // update loading flag
                            _isShowLoading = true
                        }
            }
        }
    }

    fun hideLoading() {
        _loadingDialog?.let { dialog ->
            if (dialog.isAdded)
                dialog.dismissAllowingStateLoss()
        }
    }

    /**
     * Observe flow data from lifeCycle's [CoroutineScope] of activity
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