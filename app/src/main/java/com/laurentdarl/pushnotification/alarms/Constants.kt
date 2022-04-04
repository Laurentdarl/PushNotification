package com.laurentdarl.pushnotification.alarms

import java.util.concurrent.atomic.AtomicInteger

object Constants {
    const val ACTION_SET_EXACT_ALARM = "ACTION_SET_EXACT_ALARM"
    const val EXTRA_EXACT_ALARM_TIME = "EXTRA_EXACT_ALARM_TIME"
    const val ACTION_SET_REPEATING_ALARM = "ACTION_SET_REPEATING_ALARM"

    private val seed = AtomicInteger()
    fun getRandomInt(): Int = seed.getAndIncrement() + System.currentTimeMillis().toInt()

}