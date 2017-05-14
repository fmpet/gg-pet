package cn.tonlyshy.app.fmpet.utility;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

/**
 * Created by liaowm5 on 17/5/14.
 */

public class PermissionCheckerer {
    public static boolean checkNotificationListenerPermission(Context context){
        Set<String> packageNames= NotificationManagerCompat.getEnabledListenerPackages(context);
        if(packageNames.contains(context.getPackageName())){
            return true;
        }
        return false;
    }
    public static boolean checkFloatWindowPermission(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //AppOpsManager添加于API 19
            return checkOps(context);
        } else {
            //4.4以下一般都可以直接添加悬浮窗
            return true;
        }
    }
    public static void requestNotificationListenerPermission(Context context){
        context.startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }
    public static void requestFloatWindowPermission(Context context){
        if(RomUtils.isDomesticSpecialRom()){
            if(RomUtils.isCoolPadRom()){
                RomUtils.applyCoolpadPermission(context);
            }
            else if(RomUtils.isHuaweiRom()){
                RomUtils.applyHuaweiPermission(context);
            }
            else if(RomUtils.isLenovoRom()){
                RomUtils.applyLenovoPermission(context);
            }
            else if(RomUtils.isLetvRom()){
                RomUtils.applyLetvPermission(context);
            }
            else if(RomUtils.isMeizuRom()){
                RomUtils.applyMeizuPermission(context);
            }
            else if(RomUtils.isMiuiRom()){
                RomUtils.applyMiuiPermission(context);
            }
            else if(RomUtils.isOppoRom()){
                RomUtils.applyOppoPermission(context);
            }
            else if(RomUtils.isVivoRom()){
                RomUtils.applyVivoPermission(context);
            }
            else if(RomUtils.checkIs360Rom()){
                RomUtils.apply360Permission(context);
            }
        }else{
            applyCommonPermission(context);
        }
    }


    private static void applyCommonPermission(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean checkOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = (Integer) method.invoke(object, arrayOfObject1);
            //4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
            return m == AppOpsManager.MODE_ALLOWED || !RomUtils.isDomesticSpecialRom();
        } catch (Exception ignore) {
        }
        return false;
    }
}
