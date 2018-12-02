package my.dzeko.timetable.services

import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import my.dzeko.timetable.entities.Day
import my.dzeko.timetable.models.AbstractModel
import my.dzeko.timetable.utils.DateUtils
import my.dzeko.timetable.utils.NotificationUtils
import my.dzeko.timetable.wrappers.DatabaseWrapper
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper

private const val SCHEDULE_SERVICE_ID = 30

class ScheduleNotificationService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        initAppComponent()
        val currDate = DateUtils.getTomorrowMonthAndDay()
        val schedule = AbstractModel.getModel().selectedSchedule
        var today : Day? = null
        for (day in schedule.schedule){
            if (day.date == currDate) {
                today = day
                break
            }
        }
        today?.let {day ->
            NotificationUtils.showNextSubjectNotification(applicationContext, day)
        }
    }

    private fun initAppComponent() {
        DatabaseWrapper.initialize(applicationContext)
        SharedPreferencesWrapper.initialize(applicationContext)
    }

    companion object {
        fun enqueueJob(context :Context){
            enqueueWork(context, ScheduleNotificationService::class.java, SCHEDULE_SERVICE_ID, Intent())
        }
    }
}
