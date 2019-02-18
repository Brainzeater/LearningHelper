package com.kushnarev.learnproject.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.kushnarev.learnproject.MainActivity;
import com.kushnarev.learnproject.R;
import com.kushnarev.learnproject.StudyChunkActivity;

public class NotificationUtils {
    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int STUDY_TIMER_REMINDER_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int STUDY_TIMER_REMINDER_PENDING_INTENT_ID = 3417;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private static final String STUDY_TIMER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    // This method will create a notification for charging. It might be helpful
    // to take a look at this guide to see an example of what the code in this method will look like:
    // https://developer.android.com/training/notify-user/build-notification.html
    public static void remindUser(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    STUDY_TIMER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, STUDY_TIMER_REMINDER_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
//                .setLargeIcon(largeIcon(context))
//                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                        .setContentTitle("Good Job!")
//                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                        .setContentText("Time for a break!")
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(
//                        context.getString(R.string.charging_reminder_notification_body)))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()

        notificationManager.notify(STUDY_TIMER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
//        startActivityIntent.putExtra(StudyChunkActivity.EXTRA_CHUNK_ID, chunkId);

        return PendingIntent.getActivity(
                context,
                STUDY_TIMER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
