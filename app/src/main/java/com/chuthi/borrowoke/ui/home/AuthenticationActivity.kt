package com.chuthi.borrowoke.ui.home

import android.os.Build
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.AuthModel
import com.chuthi.borrowoke.databinding.ActivityAuthenticationBinding
import kotlinx.coroutines.CoroutineScope

class AuthenticationActivity(override val viewModel: BaseViewModel? = null) :
    BaseActivity<ActivityAuthenticationBinding, BaseViewModel>(ActivityAuthenticationBinding::inflate) {

    override fun setupUI() {
        val authModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.extras?.getParcelable("AUTH_DATA", AuthModel::class.java)
        } else {
            intent?.extras?.getParcelable("AUTH_DATA") as? AuthModel
        }

        with(binding) {
            tvAuthTitle.text = authModel?.name ?: "nothing"
        }
    }

    override fun onObserveData(): (suspend CoroutineScope.() -> Unit) = {
    }

}