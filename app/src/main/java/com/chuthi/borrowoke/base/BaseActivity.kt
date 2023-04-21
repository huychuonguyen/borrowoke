package com.chuthi.borrowoke.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.ext.repeatOnLifecycle
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.enums.HttpError
import com.chuthi.borrowoke.other.enums.asString
import com.chuthi.borrowoke.ui.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> :
    LifecycleObserverActivity() {

    private lateinit var _binding: VB
    protected val binding: VB
        get() = _binding


    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@BaseActivity.handleOnBackPressed()
        }
    }

    /**
     * callback result after launch [registerForActivityResult]
     */
    private var onActivityResult: ((ActivityResult?) -> Unit)? = null

    /**
     * launcher to start activity and get callback result
     */
    private val resultLauncher =
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

    open fun onArgumentsSaved(arguments: Bundle?) {}

    protected abstract fun getViewBinding(): VB

    /**
     * - Attach other viewModels if activity have more one viewModel
     * besides the main [viewModel] variable.
     * - Notice: Do not add the [viewModel] variable into this list.
     */
    open fun setMoreViewModels(): List<BaseViewModel> = emptyList()

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
        _binding = getViewBinding()
        // raise arguments saved callback
        onArgumentsSaved(intent?.extras)
        // set layout id
        setContentView(binding.root)
        // setup ui
        setupUI()
        // init dialogs
        initDialogs()
        // register back pressed
        registerBackPressed(backPressedCallback)
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
                        .commit()
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
                dialog.dismiss()
        }
    }

    /**
     * Observe flow data from lifeCycle's [CoroutineScope] of activity
     */
    private fun observeData() {
        val viewModels = setMoreViewModels().plus(viewModel)
        repeatOnLifecycle {
            /* // observe loading
             launch {
                 viewModel?.isLoading?.collectLatest { isLoading ->
                     if (isLoading) showLoading()
                     else hideLoading()
                 }
             }
             // observe error
             launch {
                 viewModel?.error?.collect { commonError ->
                     val errorMess = commonError.message.asString(this@BaseActivity)

                     when (commonError) {
                         is HttpError.Unauthorized401 -> {
                             // open authentication activity
                             //openActivity(targetActivity = AuthenticationActivity::class.java)
                         }

                         else -> showToast(errorMess)
                     }
                 }
             }*/
            // also observe other viewModels besides the main viewModel
            viewModels.forEach { viewModel ->
                // observe loading
                launch {
                    viewModel?.isLoading?.collectLatest { isLoading ->
                        if (isLoading) showLoading()
                        else hideLoading()
                    }
                }
                // observe error
                launch {
                    viewModel?.error?.collect { commonError ->
                        val errorMess = commonError.message.asString(this@BaseActivity)

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
            // raise observe data on coroutine
            onObserveData()?.invoke(this)
        }
    }

    /**
     * open an activity with result
     */
    fun <T : AppCompatActivity> openActivity(
        targetActivity: Class<T>,
        data: Bundle? = null,
        result: ((ActivityResult?) -> Unit)? = null
    ) {
        onActivityResult = result
        val targetIntent = Intent(this, targetActivity).apply {
            data?.let { putExtras(it) }
        }
        resultLauncher.launch(targetIntent)
    }
}