package com.chuthi.borrowoke.ui.auth

import android.content.Intent
import android.os.Bundle
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.AuthModel
import com.chuthi.borrowoke.databinding.ActivityAuthenticationBinding
import com.chuthi.borrowoke.ext.getData
import com.chuthi.borrowoke.ext.putData

class AuthenticationActivity(override val viewModel: BaseViewModel? = null) :
    BaseActivity<ActivityAuthenticationBinding, BaseViewModel>() {

    private var authModel: AuthModel? = null
    private var intData: Int? = null
    private var floatData: Float? = null

    override fun getViewBinding() = ActivityAuthenticationBinding.inflate(layoutInflater)

    override fun onArgumentsSaved(arguments: Bundle?) {
        arguments?.run {
            authModel = getData("AUTH_DATA")
            intData = getData("INT_DATA") ?: 0
            floatData = getData("FLOAT_DATA") ?: 0f
        }
    }

    override fun setupUI() {
        with(binding) {
            tvAuthTitle.text =
                (authModel?.name ?: "nothing").plus(" | Int: $intData | Float: $floatData")
        }
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