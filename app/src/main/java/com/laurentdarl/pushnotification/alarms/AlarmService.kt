package com.laurentdarl.pushnotification.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.laurentdarl.pushnotification.alarms.Constants.ACTION_SET_EXACT_ALARM
import com.laurentdarl.pushnotification.alarms.Constants.ACTION_SET_REPEATING_ALARM
import com.laurentdarl.pushnotification.alarms.Constants.EXTRA_EXACT_ALARM_TIME
import com.laurentdarl.pushnotification.alarms.Constants.getRandomInt

class AlarmService(val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun setExactAlarm(timeInMillis: Long) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = ACTION_SET_EXACT_ALARM
                    putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            )
        )
    }

    fun setRepeatingAlarm(timeInMillis: Long) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = ACTION_SET_REPEATING_ALARM
                    putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            )
        )
    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent)
            }
        }
    }

    private fun getIntent(): Intent = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) = PendingIntent.getBroadcast(
        context,
        getRandomInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}