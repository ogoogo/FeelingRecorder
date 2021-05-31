package com.pico.ogoshi.feelingrecorder

import android.app.AlarmManager
import android.app.PendingIntent
import java.util.*

class Alarm {
    var alarmMgr:AlarmManager? =null
    lateinit var alarmIntent: PendingIntent

    // Set the alarm to start at approximately 2:00 p.m.
    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 7)
    }

    // With setInexactRepeating(), you have to use one of the AlarmManager interval
    // constants--in this case, AlarmManager.INTERVAL_DAY.
    

}