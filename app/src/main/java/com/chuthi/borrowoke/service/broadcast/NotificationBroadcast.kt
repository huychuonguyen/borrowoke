package com.chuthi.borrowoke.service.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.chuthi.borrowoke.data.repo.UserRepo
import com.chuthi.borrowoke.other.NOTIFICATION_GROUP_ID
import com.chuthi.borrowoke.util.cancelNotification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * Broadcast receiver to receive button click
 * on custom notification.
 */
class NotificationBroadcast : BroadcastReceiver() {

    private val userRepo: UserRepo by inject(UserRepo::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let { intent ->
            val data = intent.extras?.getString(BROADCAST_LOVE_ARG) ?: ""
            Log.i(BROADCAST_LOVE_ARG, data)
            GlobalScope.launch(Dispatchers.IO) {
                userRepo.deleteUser(userId = 0)
            }
            cancelNotification(p0, NOTIFICATION_GROUP_ID)
        }
    }

    companion object {
        const val BROADCAST_LOVE_ARG = "LOVE_ARG"
    }
}