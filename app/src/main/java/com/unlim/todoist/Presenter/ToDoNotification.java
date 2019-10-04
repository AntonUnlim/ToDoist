package com.unlim.todoist.Presenter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.unlim.todoist.R;
import com.unlim.todoist.View.ToDoListActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ToDoNotification extends Notification {
    private Notification.Builder nBuilder;
    private NotificationManager notificationManager;
    private final String CHANNEL_ID = "ToDo_channel_ID";

    public ToDoNotification(Context context) {
        Intent toDoListActivityIntent = new Intent(context, ToDoListActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(toDoListActivityIntent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        this.nBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.baseline_notification_important_black_18dp)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public void create(int notificationID, String title, String text) {
        nBuilder.setContentTitle(title);
        nBuilder.setContentText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "ToDoist notification channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            nBuilder.setChannelId(CHANNEL_ID);
        }
        notificationManager.notify(notificationID, nBuilder.build());
    }

    public void cancel(int notificationID) {
        notificationManager.cancel(notificationID);
    }
}
