package com.chuthi.borrowoke.ui.main

import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.data.model.AuthModel
import com.chuthi.borrowoke.databinding.ActivityMainBinding
import com.chuthi.borrowoke.ext.backToSystemHome
import com.chuthi.borrowoke.ext.getData
import com.chuthi.borrowoke.ext.putData
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.ui.auth.AuthenticationActivity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModel()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupUI() {
        // get fcm token
        getFcmToken()
        // register callback
        registerCallback()
    }

    private fun openAuthentication() {
        // delay 2s to open AuthenticationActivity
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            openActivity(targetActivity = AuthenticationActivity::class.java,
                data = Bundle().apply {
                    putData(
                        "AUTH_DATA" to AuthModel(
                            name = "Medal Auth"
                        ), "INT_DATA" to 500, "FLOAT_DATA" to 5f
                    )
                }) {
                it?.let { result ->
                    val resultInt =
                        result.data?.extras?.getData("AUTH_RESULT") ?: ""
                    showToast(resultInt)
                }
            }
        }, 2000)
    }

    private fun registerCallback() {
        binding.tvQuote.setOnClickListener {
            //addHomeFragment()
        }
    }

    override fun observeFlowData(): CoroutineScope.() -> Unit = {
        /*  launch {
              viewModel.dummyData.collect { data ->
                  Log.i("dummyData", data.toString())
              }
          }*/

        /*launch {
            viewModel.dummyData2.collect {
                Log.i("dummyData2", it)
                showToast(it)
            }
        }

        launch {
            viewModel.errorChannel.collectLatest { error ->
                showToast(error)
            }
        }*/

        /* launch {
             viewModel.errorSharedFlow.collectLatest { errorShared ->
                 showToast(errorShared)
             }
         }*/
    }

    override fun handleOnBackPressed() = backToSystemHome()

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) task.result?.let {
                val fcmToken = it
                Log.i("fcmToken", fcmToken)
            }
        }
    }

}