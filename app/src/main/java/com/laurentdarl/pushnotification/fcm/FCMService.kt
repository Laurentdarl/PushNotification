package com.laurentdarl.pushnotification.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.laurentdarl.pushnotification.MainActivity
import com.laurentdarl.pushnotification.R

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        createNotificationChannel()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (message != null) {
            val title = message.notification!!.title.toString()
            val body = message.notification!!.body.toString()
            generateNotification(title, body, "")
        }
    }

    private fun generateNotification(title: String, body: String, type: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, intent, PendingIntent.FLAG_ONE_SHOT
        )

        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())
    }

    fun createNotificationChannel() {
        val notificationDescription = "Notification by topic test"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = notificationDescription
                lightColor = Color.RED
                enableLights(true)
                enableVibration(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}