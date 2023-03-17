package com.chuthi.borrowoke.ui.home

import android.content.Intent
import android.os.Bundle
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.AuthModel
import com.chuthi.borrowoke.databinding.ActivityAuthenticationBinding
import com.chuthi.borrowoke.ext.getData
import kotlinx.coroutines.CoroutineScope

class AuthenticationActivity(override val viewModel: BaseViewModel? = null) :
    BaseActivity<ActivityAuthenticationBinding, BaseViewModel>(ActivityAuthenticationBinding::inflate) {

    private var authModel: AuthModel? = null

    override fun onArgumentsSaved(arguments: Bundle?) {
        authModel = arguments?.getData("AUTH_DATA")
    }

    override fun setupUI() {
        with(binding) {
            tvAuthTitle.text = authModel?.name ?: "nothing"
        }
    }

    override fun onObserveData(): (suspend CoroutineScope.() -> Unit) = {
    }

    override fun handleOnBackPressed() {
        setResult(1995, Intent().apply {
            putExtra("AUTH_RESULT", 1995)
        })
        finish()
    }
}