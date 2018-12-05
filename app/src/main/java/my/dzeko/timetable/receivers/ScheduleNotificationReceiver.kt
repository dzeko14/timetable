package my.dzeko.timetable.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import my.dzeko.timetable.notifications.NotificationScheduler
import my.dzeko.timetable.services.ScheduleNotificationService
import my.dzeko.timetable.utils.NotificationUtils
import my.dzeko.timetable.utils.REMOVE_NOTIFICATION_ACTION

class ScheduleNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == REMOVE_NOTIFICATION_ACTION) {
                NotificationUtils.cancelNotifications(context!!)
                return
            }
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, true)){
                NotificationScheduler.setSubjectNotificationSchedule(context!!)
                return
            }
        }
        ScheduleNotificationService.enqueueJob(context!!)
    }
}