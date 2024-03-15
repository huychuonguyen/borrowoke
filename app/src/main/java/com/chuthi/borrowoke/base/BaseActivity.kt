package com.chuthi.borrowoke.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat.Type
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.base.interfaces.LifecycleObserverActivity
import com.chuthi.borrowoke.ext.getSystemNavigationBarHeight
import com.chuthi.borrowoke.ext.setDecorFitsSystemWindows
import com.chuthi.borrowoke.ext.setNavigationBarColor
import com.chuthi.borrowoke.ext.setStatusBarColor
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.ui.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlin.properties.Delegates


/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 24/04/2023
- Project : Base Kotlin
 **************************************/
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> :
    LifecycleObserverActivity() {

    @delegate:ColorInt
    private var defaultStatusBarColor by Delegates.notNull<Int>()


    @delegate:ColorInt
    private var defaultNavigationBarColor by Delegates.notNull<Int>()

    protected val binding by lazy {
        getViewBinding()
    }

    abstract fun getViewBinding(): VB

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

    /**
     * - Attach other viewModels if activity have more one viewModel
     * besides the main [viewModel] variable.
     * - Notice: Do not add the [viewModel] variable into this list.
     */
    open fun getViewModels(): List<BaseViewModel> = emptyList()

    abstract fun setupUI()


    /**
     * Override this method to handle on back pressed new api
     */
    open fun handleOnBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this)
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
        // get default status and navigation bar color
        initDefaultStatusAndNavigationBar()

        //

        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            val statusBarSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                windowInsets.getInsets(Type.systemBars()).bottom
            } else {
                windowInsets.systemWindowInsetBottom
            }

            showToast("navigationHeight New: $statusBarSize")
            windowInsets
        }
    }

    open fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        return super.dispatchTouchEvent(ev)
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

    private fun initDefaultStatusAndNavigationBar() {
        defaultStatusBarColor = window?.statusBarColor ?: Color.TRANSPARENT
        defaultNavigationBarColor = window?.navigationBarColor ?: Color.TRANSPARENT
    }

    override fun showLoading() {
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

    override fun hideLoading() {
        _loadingDialog?.let { dialog ->
            if (dialog.isAdded)
                dialog.dismiss()
        }
    }

    /**
     * Observe flow data from lifeCycle's [CoroutineScope] of activity
     */
    private fun observeData() {
        val viewModels =
            getViewModels().plus(viewModel).distinct()
        //  observe loading, error on each viewModel
        observeData(
            context = this,
            lifecycleOwner = this,
            viewModels = viewModels
        )
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


    fun transparentStatusAndNavigation(on: Boolean) {
        // make full transparent statusBar
        setStatusBarColor(Color.TRANSPARENT)
        setNavigationBarColor(Color.TRANSPARENT)
        setDecorFitsSystemWindows(on)
    }

    /**
     * Reset status/navigation bar color to default.
     */
    fun resetDefaultStatusAndNavigationColor() {
        setStatusBarColor(defaultStatusBarColor)
        setNavigationBarColor(defaultNavigationBarColor)
        setDecorFitsSystemWindows(true)
    }

}