package com.chuthi.borrowok.service.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.chuthi.borrowok.R
import com.chuthi.borrowok.other.NOTIFICATION_GROUP_ID
import com.chuthi.borrowok.other.NOTIFICATION_GROUP_KEY
import com.chuthi.borrowok.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 11/02/2022
- Project : Borrowok
 **************************************/
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.data)
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    override fun handleIntent(intent: Intent) {
        try {
            if (intent.extras != null) {
                val build = RemoteMessage.Builder("MyFirebaseMessagingService")
                for (key in intent.extras!!.keySet()) {
                    build.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(build.build())
            } else super.handleIntent(intent)
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }

    private fun sendRegistrationToServer(token: String?) {
        val fcmToken = token
        // token?.let { fcmToken = token }
    }

    private fun sendNotification(messageBody: Map<String, String>) {
        messageBody["body"]?.let {
            // get target activity is MainActivity
            // to ensure both case:
            // - Click notify when app not running
            // - Click notify when app running
            val targetActivity = MainActivity::class.java
            // create intent to put data
            val intent = Intent(this, targetActivity)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            // get random id by current time on millis second
            val notifyId = Date().time.toInt()
            // then parse into [NotificationType] enum
            //val notifyType = getNotifyType(messageBody)
            // put notification type
            //intent.putExtra(NOTIFICATION_TYPE, notifyType)

            intent.action = Intent.ACTION_MAIN
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addParentStack(targetActivity)
            stackBuilder.addNextIntent(intent)

            val pendingIntent = PendingIntent.getActivity(
                this,
                // Add requestCode = notifyId as random integer
                // to ensure this pending intent is unique.
                // To be sure the data only putExtras on difference intent
                // so, you can get this data from this intent with the same key [NOTIFICATION_TYPE]
                // This is so important to handle case push notification when app is running
                notifyId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val channelId = getString(R.string.default_notification_channel_id)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(messageBody["title"] ?: "")
                .setContentText(messageBody["body"] ?: "")
                .setAutoCancel(true)
                .setPriority(PRIORITY_MAX)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val badgeCount = parseBadge(messageBody["badge"])
            when {
                badgeCount != null && badgeCount >= 0 ->
                    notificationBuilder.setNumber(badgeCount)
                else -> notificationManager.cancelAll()
            }

            val groupBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(PRIORITY_MAX)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSound(null)
                .setGroupSummary(true)

            val channel = NotificationChannel(
                channelId,
                "Channel human readable title", IMPORTANCE_HIGH
            )
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)

            // put notification
            notificationManager.apply {
                if (activeNotifications.none { it.id == NOTIFICATION_GROUP_ID }) notify(
                    NOTIFICATION_GROUP_ID,
                    groupBuilder.build()
                )
                notify(notifyId, notificationBuilder.build())
            }
        }
    }

    /**
     * Get notification type enum based on response
     */
    /*private fun getNotifyType(messageBody: Map<String, String>): NotificationType {
        // get notification type from messageBody
        val notifyType = messageBody["type"] ?: "1"
        return when (notifyType.toInt()) {
            TYPE_NOTIFY_LIST -> NotificationType.NotifyList
            TYPE_DETAIL -> {
                val url = messageBody["url"]
                NotificationType.Detail(
                    url = url
                )
            }
            else -> NotificationType.Normal
        }
    }*/

    private fun parseBadge(badge: String?): Int? {
        return try {
            badge?.toInt()
        } catch (e: Exception) {
            print(e.toString())
            null
        }
    }

}
