package com.sakurov.notes;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sakurov.notes.fragments.EditNotificationFragment;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);

        com.sakurov.notes.entities.Notification notification = intent
                .getBundleExtra(EditNotificationFragment.NOTIFICATION)
                .getParcelable(EditNotificationFragment.NOTIFICATION);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notifications_active_white_48dp)
                .setContentTitle(context.getString(R.string.title_alert_notification))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo(context.getString(R.string.alert_info));

        if (notificationManager != null && notification != null) {
            builder.setWhen(notification.getTimeInMillis())
                    .setContentText(notification.getText());

            notificationManager.notify((int) notification.getId(), builder.build());
        }
    }
}