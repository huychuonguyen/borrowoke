package com.chuthi.borrowoke.ui.auth

import android.content.Intent
import android.os.Bundle
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.AuthModel
import com.chuthi.borrowoke.data.model.ParcelizeData
import com.chuthi.borrowoke.databinding.ActivityAuthenticationBinding
import com.chuthi.borrowoke.ext.getData
import com.chuthi.borrowoke.ext.putData
import kotlinx.coroutines.CoroutineScope

class AuthenticationActivity(override val viewModel: BaseViewModel? = null) :
    BaseActivity<ActivityAuthenticationBinding, BaseViewModel>(ActivityAuthenticationBinding::inflate) {

    private var authModel: AuthModel? = null
    private var intData: Int? = null
    private var floatData: Float? = null

    override fun onArgumentsSaved(arguments: Bundle?) {
        arguments?.run {
            authModel = getData("AUTH_DATA")
            intData = getData<ParcelizeData>("INT_DATA")?.getRawValue() ?: 0
            floatData = getData<ParcelizeData>("FLOAT_DATA")?.getRawValue() ?: 0f
        }
    }

    override fun setupUI() {
        with(binding) {
            tvAuthTitle.text =
                (authModel?.name ?: "nothing").plus(" | Int: $intData | Float: $floatData")
        }
    }

    override fun onObserveData(): (suspend CoroutineScope.() -> Unit) = {
    }

    override fun handleOnBackPressed() {
        setResult(1995, Intent().apply {
            putExtras(Bundle().apply {
                putData("AUTH_RESULT" to "Back nè")
            })
        })
        finish()
    }
}