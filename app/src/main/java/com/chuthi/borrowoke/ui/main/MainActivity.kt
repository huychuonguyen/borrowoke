package com.chuthi.borrowoke.ui.main

import android.util.Log
import android.view.LayoutInflater
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun setViewBinding(inflater: LayoutInflater) =
        ActivityMainBinding.inflate(inflater)

    override val viewModel: MainViewModel by viewModel()

    override fun setupUI() {
        // get fcm token
        getFcmToken()
        // register callback
        registerCallback()
        // observe data
        onObserveData()
    }

    private fun addHomeFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.frameMain,
                HomeFragment.newInstance("Medal Gumi"),
                HomeFragment.TAG
            )
            .commit()
    }

    private fun registerCallback() {
        binding.tvQuote.setOnClickListener {
            addHomeFragment()
        }
    }

    override fun onObserveData(): suspend CoroutineScope.() -> Unit = {
         launch {
             viewModel.dummyData.collect { data ->
                 Log.i("dummyData", data.toString())
             }
         }

         launch {
             viewModel.dummyData2.collect {
                 Log.i("dummyData2", it)
             }
         }
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) task.result?.let {
                val fcmToken = it
                Log.i("fcmToken", fcmToken)
            }
        }
    }
}