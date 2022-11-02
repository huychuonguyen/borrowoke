package com.chuthi.borrowok.ui

import android.util.Log
import com.chuthi.borrowok.R
import com.chuthi.borrowok.base.BaseActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_main

    override fun setupUI() {
        getFcmToken()
    }

    fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) task.result?.let {
                val fcmToken = it
                Log.i("fcmToken", fcmToken)
            }
        }
    }

}