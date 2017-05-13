package cn.tonlyshy.app.fmpet.plugin;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by liaowm5 on 17/5/14.
 */

public class NotificationMonitor extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);////
        Bundle extras=sbn.getNotification().extras;
        Log.d(TAG, "onNotificationPosted: "+extras.getString(Notification.EXTRA_TITLE)+extras.get(Notification.EXTRA_TEXT));
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
