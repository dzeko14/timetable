package my.dzeko.timetable.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import my.dzeko.timetable.receivers.ScheduleNotificationReceiver
import my.dzeko.timetable.utils.DateUtils
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper
import android.content.Context.ALARM_SERVICE



private const val SCHEDULE_NOTIFICATION_REQUEST_CODE = 131432

object NotificationScheduler {
    fun setSubjectNotificationSchedule(context: Context){

        val receiver = ComponentName(context, ScheduleNotificationReceiver::class.java)
        val packetManager = context.packageManager.apply {
            setComponentEnabledSetting( receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            )
        }

        val intent = Intent(context, ScheduleNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
                SCHEDULE_NOTIFICATION_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time = DateUtils.getScheduleTimeInLong(SharedPreferencesWrapper.getInstance().subjectNotificationTime)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time,
                AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun cancelSubjectNotificationSchedule(context: Context) {
        val receiver = ComponentName(context, ScheduleNotificationReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
        val intent1 = Intent(context, ScheduleNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
                SCHEDULE_NOTIFICATION_REQUEST_CODE,
                intent1,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}