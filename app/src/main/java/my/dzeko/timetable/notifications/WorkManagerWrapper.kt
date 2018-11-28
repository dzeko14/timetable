package my.dzeko.timetable.notifications

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import my.dzeko.timetable.utils.DateUtils
import java.util.concurrent.TimeUnit

private const val SUBJECT_NOTIFICATION_TAG = "Notification tag"
object WorkManagerWrapper {
    fun enqueueSubjectNotification(){
        val workManager = WorkManager.getInstance()
        val workerRequest = OneTimeWorkRequestBuilder<SubjectNotificationWorker>()
                .addTag(SUBJECT_NOTIFICATION_TAG)
                .setInitialDelay(getDuration(), TimeUnit.MILLISECONDS)
                .build()
        workManager.enqueue(workerRequest)
    }

    private fun getDuration() :Long {
        //TODO("Implement")
        return DateUtils.countNavNotificationTime()
    }

    fun removeSubjectNotification(){
        val workManager = WorkManager.getInstance()
        workManager.cancelAllWorkByTag(SUBJECT_NOTIFICATION_TAG)
    }
}