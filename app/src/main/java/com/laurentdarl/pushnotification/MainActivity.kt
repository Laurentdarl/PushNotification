package com.laurentdarl.pushnotification

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.laurentdarl.pushnotification.alarms.AlarmService
import com.laurentdarl.pushnotification.databinding.ActivityMainBinding
import java.util.*

private lateinit var analytics: FirebaseAnalytics
private lateinit var binding: ActivityMainBinding
private lateinit var alarmService: AlarmService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analytics = Firebase.analytics
        alarmService = AlarmService(this)

        binding.btnGeneralNotification.setOnClickListener {
           setAlarm { timeInMillis -> alarmService.setExactAlarm(timeInMillis) }
        }

        binding.btnTargetedNotification.setOnClickListener {
            setAlarm { alarmService.setRepeatingAlarm(it) }
        }
    }

    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                this@MainActivity,
                0,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)

                    TimePickerDialog(
                        this@MainActivity,
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(this)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(this)

        AlertDialog.Builder(this)
            .setTitle("Alarm scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + "" + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year
        val calendar = Calendar.getInstance()

        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }
}