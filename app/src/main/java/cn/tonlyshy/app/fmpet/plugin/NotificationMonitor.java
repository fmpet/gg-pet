package cn.tonlyshy.app.fmpet.plugin;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.Calendar;

import cn.tonlyshy.app.fmpet.core.FloatViewManager;
import cn.tonlyshy.app.fmpet.utility.VibratorUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by liaowm5 on 17/5/14.
 */

public class NotificationMonitor extends NotificationListenerService {

    public static final String ACTION_SET_ALARM = "cn.tonlyshy.app.fmpet.plugin.NotificationMonitor.ACTION_SET_ALARM";
    public static final String ACTION_ALARM_TRIGGER = "cn.tonlyshy.app.fmpet.plugin.NotificationMonitor.ACTION_ALARM_TRIGGER";
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_SET_ALARM)) {
                Log.d("TAG", "succeeded " + intent.getAction());
                Log.d("TAG", "" + intent.getIntExtra("year", 0) + " " + intent.getIntExtra("hour", 0));
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent1 = new Intent(this, NotificationMonitor.class);
                intent1.setAction(ACTION_ALARM_TRIGGER);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, intent.getIntExtra("year", 0));
                calendar.set(Calendar.MONTH, intent.getIntExtra("month", 0));
                calendar.set(Calendar.DAY_OF_MONTH, intent.getIntExtra("day", 0));
                calendar.set(Calendar.HOUR_OF_DAY, intent.getIntExtra("hour", 0));
                calendar.set(Calendar.MINUTE, intent.getIntExtra("minute", 0));
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Log.d("time", calendar.getTimeInMillis() - System.currentTimeMillis() + "");
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            else if (intent.getAction().equals(ACTION_ALARM_TRIGGER)) {
                Log.d("Vibrate", "succeeded");
                VibratorUtil.Vibrate(this, new long[] {1000, 1000, 1000, 1000}, false);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
