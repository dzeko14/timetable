package my.dzeko.timetable.notifications

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val SUBJECT_NOTIFICATION_TAG = "Notification tag"
object WorkManagerWrapper {
    fun enqueueSubjectNotification(){
        val workManager = WorkManager.getInstance()
        val workerRequest = OneTimeWorkRequestBuilder<SubjectNotificationWorker>()
                .addTag(SUBJECT_NOTIFICATION_TAG)
                .setInitialDelay(getDuration(), TimeUnit.SECONDS)
                .build()
        workManager.enqueue(workerRequest)
    }

    private fun getDuration() :Long {
        //TODO("Implement")
        return 10
    }

    fun removeSubjectNotification(){
        val workManager = WorkManager.getInstance()
        workManager.cancelAllWorkByTag(SUBJECT_NOTIFICATION_TAG)
    }
}