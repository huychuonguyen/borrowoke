package com.chuthi.borrowoke.service.firebase

import android.content.Intent
import com.chuthi.borrowoke.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 11/02/2022
- Project : Borrowok
 **************************************/
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(this, remoteMessage.data)
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    override fun handleIntent(intent: Intent) {
        try {
            intent.extras?.let { extras ->
                val build = RemoteMessage
                    .Builder("MyFirebaseMessagingService")
                    .apply {
                        extras.keySet().forEach { key ->
                            addData(key, extras.getString(key))
                        }
                    }
                onMessageReceived(build.build())
            } ?: super.handleIntent(intent)
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }

    private fun sendRegistrationToServer(token: String?) {
        val fcmToken = token
        // token?.let { fcmToken = token }
    }

}
