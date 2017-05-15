package cn.tonlyshy.app.fmpet.plugin;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import cn.tonlyshy.app.fmpet.core.FloatViewManager;

import static android.content.ContentValues.TAG;

/**
 * Created by liaowm5 on 17/5/14.
 */

public class NotificationMonitor extends NotificationListenerService {

    private FloatViewManager manager;
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);////
        if (isTencent(sbn)) {
            Bundle extras = sbn.getNotification().extras;
            Log.d(TAG, "onNotificationPosted: " + extras.getString(Notification.EXTRA_TITLE) + extras.get(Notification.EXTRA_TEXT));
            try {
                manager = FloatViewManager.getInstance(this);
                manager.setBubbleViewText(extras.getString(Notification.EXTRA_TITLE) + " : " + extras.get(Notification.EXTRA_TEXT), extras.get(Notification.EXTRA_LARGE_ICON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isTencent(StatusBarNotification sbn){
        return ("com.tencent.mm".equals(sbn.getPackageName())||"com.tencent.tim".equals(sbn.getPackageName())||"com.tencent.mobileqq".equals(sbn.getPackageName()));
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
