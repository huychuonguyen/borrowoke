package com.chuthi.borrowoke.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.ext.repeatOnLifecycle
import com.chuthi.borrowoke.ui.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>(
    private val viewBindingInflater: (
        inflater: LayoutInflater
    ) -> VB
) : LifecycleObserverActivity() {

    private lateinit var _binding: VB
    protected val binding: VB
        get() = _binding

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@BaseActivity.handleOnBackPressed()
        }
    }

    /**
     * callback result after call startActivityResult
     */
    private var onActivityResult: ((ActivityResult?) -> Unit)? = null

    /**
     * launcher to start activity and get callback result
     */
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onActivityResult?.invoke(it)
        }

    /**
     * Loading dialog
     */
    private var _loadingDialog: LoadingDialog? = null

    /**
     * Flag to check is [LoadingDialog] showing
     */
    private var _isShowLoading = false

    protected abstract val viewModel: VM?

    abstract fun setupUI()

    abstract fun onObserveData(): (suspend CoroutineScope.() -> Unit)?

    /**
     * Override this method to handle on back pressed new api
     */
    open fun handleOnBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this)
        _binding = viewBindingInflater(layoutInflater)
        // set layout id
        setContentView(binding.root)
        // register back pressed
        registerBackPressed(backPressedCallback)
        // init dialogs
        initDialogs()
        // setup ui
        setupUI()
        // observe data
        observeData()
    }

    fun addFragmentBackPressed(lifecycleOwner: LifecycleOwner, callback: OnBackPressedCallback) {
        onBackPressedDispatcher.addCallback(
            lifecycleOwner,
            callback
        )
    }


    private fun registerBackPressed(callback: OnBackPressedCallback) {
        onBackPressedDispatcher.addCallback(
            callback
        )
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

    inline fun <reified T : AppCompatActivity> openActivity(
        data: Bundle? = null
    ) {
        val targetIntent = Intent(this, T::class.java).apply {
            data?.let { putExtras(it) }
        }
        startActivity(targetIntent)
    }

}