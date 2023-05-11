package com.chuthi.borrowoke.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.CallSuper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewbinding.ViewBinding

/**
 * Fragment with [SwipeRefreshLayout] to handle pull to refresh action.
 */
abstract class SwipeRefreshFragment<VB : ViewBinding, VM : BaseViewModel>
    : BaseFragment<VB, VM>(), OnRefreshListener {

    private var _loadingHandler: Handler? = null

    private lateinit var _swipeRefreshLayout: SwipeRefreshLayout

    /**
     * the [SwipeRefreshLayout] component to handle pull to refresh.
     */
    protected abstract fun swipeRefreshLayout(): SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init swipe refresh component
        initRefreshLayout()
        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onRefresh() {
        // set timeout to hide refreshing
        setRefreshTimeout {
            hideRefreshing()
        }
    }

    override fun onDestroyView() {
        // clear timeout callback
        clearRefreshTimeout()
        super.onDestroyView()
    }

    protected fun showRefreshing() = _swipeRefreshLayout.run {
        post {
            // show refreshing
            isRefreshing = true
        }
    }

    protected fun hideRefreshing() {
        _swipeRefreshLayout.isRefreshing = false
        // clear timeout callback
        clearRefreshTimeout()
    }

    protected fun refreshScreen() {
        _swipeRefreshLayout.run {
            post {
                // show refreshing
                showRefreshing()
                // raise action
                onRefresh()
            }
        }
    }

    /**
     * Set timeout 10s to hide loading dialog
     * @param onTimeout callback when timeout
     */
    private fun setRefreshTimeout(onTimeout: (() -> Unit)? = null) {
        _loadingHandler = Handler(Looper.getMainLooper())
        // Countdown 10s to hide loading dialog
        _loadingHandler?.postDelayed({
            (context as? BaseActivity<*, *>)?.runOnUiThread {
                // raise callback
                onTimeout?.invoke()
            }
        }, LOADING_TIMEOUT)
    }

    /**
     * Clear [_loadingHandler] callback.
     */
    private fun clearRefreshTimeout() {
        _loadingHandler?.removeCallbacksAndMessages(null)
        _loadingHandler = null
    }

    private fun initRefreshLayout() {
        // get swipe refresh component by id
        _swipeRefreshLayout = swipeRefreshLayout().apply {
            setOnRefreshListener(this@SwipeRefreshFragment)
        }
    }

    companion object {
        private const val LOADING_TIMEOUT = 10000L
    }
}