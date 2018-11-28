package my.dzeko.timetable.notifications

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import my.dzeko.timetable.models.AbstractModel
import my.dzeko.timetable.utils.DateUtils
import my.dzeko.timetable.utils.NotificationUtils
import my.dzeko.timetable.wrappers.DatabaseWrapper
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper
import java.lang.IllegalStateException

class SubjectNotificationWorker(
        context: Context,
        params :WorkerParameters
) : Worker(context, params) {
    private val mModel = AbstractModel.getModel()

    override fun doWork(): Result {
        //Init SharedPrefs wrapper in case app is close
        SharedPreferencesWrapper.initialize(applicationContext)
        //Init database wrapper in case app is close
        DatabaseWrapper.initialize(applicationContext)

        val subjectName :String? = getSubjectName()
        subjectName?.let {
            NotificationUtils.showNextSubjectNotification(applicationContext, it)
        }

        prepareNextWorker()

        return Result.SUCCESS
    }

    private fun getSubjectName(): String? {
        val schedule = mModel.selectedSchedule
        val nextDay = DateUtils.getNextDayString()
        for (day in schedule.schedule){
            if (day.date == nextDay) return day.subjects[0].subjectName
        }
        Log.d("MyLog", "There is no schedule")
        return null
    }

    private fun prepareNextWorker() {
        WorkManagerWrapper.enqueueSubjectNotification()
    }
}