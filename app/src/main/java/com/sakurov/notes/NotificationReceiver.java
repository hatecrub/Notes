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

        com.sakurov.notes.entities.Notification notification = intent
                .getBundleExtra(EditNotificationFragment.NOTIFICATION)
                .getParcelable(EditNotificationFragment.NOTIFICATION);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(notification.getTimeInMillis())
                .setSmallIcon(R.drawable.ic_notifications_active_white_48dp)
                .setContentTitle(context.getString(R.string.title_alert_notification))
                .setContentText(notification.getText())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo(context.getString(R.string.alert_info));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) notification.getId(), builder.build());
        }
    }
}
