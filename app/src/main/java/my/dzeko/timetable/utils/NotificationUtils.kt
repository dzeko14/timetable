package my.dzeko.timetable.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import my.dzeko.timetable.R
import my.dzeko.timetable.activities.MainActivity

private const val PENDING_INTENT_REQUEST_CODE = 243
private const val NOTIFICATION_CHANNEL_ID = "CHANNEL_ID"
private const val NOTIFICATION_ID = 37583

object NotificationUtils {
    private fun launchAppPendingIntent(context: Context) :PendingIntent{
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, PENDING_INTENT_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun showNextSubjectNotification(context :Context, subjectName :String) {
        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val text = "${context.getString(R.string.next_subject_notif_text)} $subjectName"

        val notificationBuilder = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setContentIntent(launchAppPendingIntent(context))
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.next_subject_notif_title))
                .setContentText(text)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}