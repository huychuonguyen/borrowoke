package com.chuthi.borrowoke.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.other.NOTIFICATION_GROUP_ID
import com.chuthi.borrowoke.other.NOTIFICATION_GROUP_KEY
import com.chuthi.borrowoke.service.broadcast.NotificationBroadcast
import com.chuthi.borrowoke.service.broadcast.NotificationBroadcast.Companion.BROADCAST_LOVE_ARG
import com.chuthi.borrowoke.ui.main.MainActivity
import java.util.Date

private val imageBlurringId = Date().time.toInt() + 1995

fun sendNotification(context: Context?, messageBody: Map<String, String>) {
    context?.run {
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
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle(messageBody["title"] ?: "")
                .setContentText(messageBody["body"] ?: "")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
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
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSound(null)
                .setGroupSummary(true)

            val channel = NotificationChannel(
                channelId,
                "Channel human readable title", NotificationManager.IMPORTANCE_HIGH
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
}

fun notifyImageBlurred(context: Context?, title: String?, message: String?) {
    context?.run {
        message?.let {
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
            val notifyId = imageBlurringId
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

            // region custom layout
            val notificationContentLayout = RemoteViews(packageName, R.layout.notification_custom)
            notificationContentLayout.apply {
                val loveIntent = Intent(this@run, NotificationBroadcast::class.java)
                    .apply {
                        putExtra(BROADCAST_LOVE_ARG, "Love button clicked")
                    }
                val lovePendingIntent = PendingIntent.getBroadcast(
                    this@run,
                    notifyId,
                    loveIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                setTextViewText(R.id.tvNotificationContent, "Love")
                setOnClickPendingIntent(R.id.tvNotificationContent, lovePendingIntent)
            }
            // custom layout expend
            val notificationContentLayoutExpend = RemoteViews(packageName, R.layout.notification_custom_expend)
            notificationContentLayoutExpend.apply {
                val loveIntent = Intent(this@run, NotificationBroadcast::class.java)
                    .apply {
                        putExtra(BROADCAST_LOVE_ARG, "Love button clicked - Expend")
                    }
                val lovePendingIntent = PendingIntent.getBroadcast(
                    this@run,
                    notifyId,
                    loveIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                setTextViewText(R.id.tvNotificationContentExpend, "Love")
                setOnClickPendingIntent(R.id.tvNotificationContentExpend, lovePendingIntent)
            }
            // endregion

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationContentLayout)
                .setCustomBigContentView(notificationContentLayoutExpend)
                .setContentTitle(title ?: "")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val groupBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSound(null)
                .setGroupSummary(true)

            val channel = NotificationChannel(
                channelId,
                "Channel human readable title", NotificationManager.IMPORTANCE_HIGH
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
}

fun notifyImageBlurring(context: Context?, title: String?, message: String?, progress: Int) {
    context?.run {
        message?.let {
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
            val notifyId = imageBlurringId
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

            val priority = if (progress in 1..99) NotificationCompat.PRIORITY_LOW
            else NotificationCompat.PRIORITY_HIGH

            val importantStatus = if (progress in 1..99) NotificationManager.IMPORTANCE_LOW
            else NotificationManager.IMPORTANCE_HIGH

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title ?: "")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(priority)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setProgress(100, progress, false)


            val groupBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(priority)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSound(null)
                .setGroupSummary(true)

            val channel = NotificationChannel(
                channelId,
                getString(R.string.channel_human_readable_title),
                importantStatus
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
}


fun cancelNotification(context: Context?, notificationID: Int) {
    context?.run {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(notificationID)
    }

}

private fun parseBadge(badge: String?): Int? {
    return try {
        badge?.toInt()
    } catch (e: Exception) {
        print(e.toString())
        null
    }
}