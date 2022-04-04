package com.laurentdarl.pushnotification.alarms

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.laurentdarl.pushnotification.MainActivity
//import br.com.goncalves.pugnotification.notification.PugNotification
import com.laurentdarl.pushnotification.R
import com.laurentdarl.pushnotification.alarms.Constants.ACTION_SET_EXACT_ALARM
import com.laurentdarl.pushnotification.alarms.Constants.ACTION_SET_REPEATING_ALARM
import com.laurentdarl.pushnotification.alarms.Constants.EXTRA_EXACT_ALARM_TIME
import com.laurentdarl.pushnotification.fcm.Constants
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action) {
            ACTION_SET_EXACT_ALARM -> {
                createNotification(context, "Set Exact Time", convertDate(timeInMillis))
            }
            ACTION_SET_REPEATING_ALARM -> {
                val cal = Calendar.getInstance().apply {
                    this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
                }
                AlarmService(context).setRepeatingAlarm(cal.timeInMillis)
                createNotification(context, "Set Repeating Time", convertDate(timeInMillis))
            }
        }
    }

    private fun createNotification(context: Context, title: String, message: String) {
        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText("Alarm time set $message")
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(0, notification.build())
        }
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
//            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}