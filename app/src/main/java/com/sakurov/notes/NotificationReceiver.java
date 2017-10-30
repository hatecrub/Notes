package com.sakurov.notes;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(intent.getLongExtra("time", 0))
                .setSmallIcon(R.drawable.ic_notifications_active_white_48dp)
                .setContentTitle("Your Notification!")
                .setContentText(intent.getStringExtra("text"))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) intent.getLongExtra("id", 0), builder.build());
    }
}
