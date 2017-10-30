package com.sakurov.notes;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        com.sakurov.notes.entities.Notification notification =
                intent.getParcelableExtra("NOT");

        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(notification.getTimeInMillis())
                .setSmallIcon(R.drawable.ic_notifications_active_white_48dp)
                .setContentTitle("Your Notification!")
                .setContentText(notification.getText())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) notification.getId(), builder.build());
    }
}
