package my.dzeko.timetable.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import my.dzeko.timetable.R
import my.dzeko.timetable.activities.MainActivity
import my.dzeko.timetable.entities.Day
import my.dzeko.timetable.receivers.ScheduleNotificationReceiver
import java.lang.StringBuilder

private const val PENDING_INTENT_REQUEST_CODE = 243
private const val PENDING_INTENT_REMOVE_NOTIFICATION_REQUEST_CODE = 252
private const val NOTIFICATION_CHANNEL_ID = "CHANNEL_ID"
private const val NOTIFICATION_ID = 37583

 const val REMOVE_NOTIFICATION_ACTION = "my.dzeko.timetable.REMOVE_ALL_NOTIFICATIONS"


object NotificationUtils {
    private fun launchAppPendingIntent(context: Context) :PendingIntent{
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, PENDING_INTENT_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun showNextSubjectNotification(context :Context, day :Day) {
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

        val sb = StringBuilder()
        for (subject in day.subjects){
            sb.append(
                    "${subject.position} ${subject.subjectName} ${subject.type}\n"
            )
        }

        val text = sb.toString()

        val notificationBuilder = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setContentIntent(launchAppPendingIntent(context))
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.next_subject_notif_title))
                .setContentText(text)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(setLargeIcon(context))
                .setDefaults(Notification.DEFAULT_SOUND)
                .addAction(R.mipmap.ic_launcher,
                        context.getString(android.R.string.ok),
                        PendingIntent.getBroadcast(context,
                                PENDING_INTENT_REMOVE_NOTIFICATION_REQUEST_CODE,
                                Intent(context, ScheduleNotificationReceiver::class.java)
                                        .apply { action =  REMOVE_NOTIFICATION_ACTION },
                                PendingIntent.FLAG_UPDATE_CURRENT)
                        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setLargeIcon(context :Context): Bitmap {
        return BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)
    }

    fun testNotification(context: Context) {
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


        val notificationBuilder = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setContentIntent(launchAppPendingIntent(context))
                .setAutoCancel(true)
                .setContentText("Test")
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun cancelNotifications(context: Context) {
        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}