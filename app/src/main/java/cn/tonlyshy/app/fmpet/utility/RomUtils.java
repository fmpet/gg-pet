package cn.tonlyshy.app.fmpet.utility;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 哦嘿嘿哈哈吼  on http://www.jianshu.com/p/2229d4a12269
 */

public class RomUtils {
    private static final String TAG = "RomUtils";

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    /**
     * 判断是否为华为系统
     */
    public static boolean isHuaweiRom() {
        String manufacturer = Build.MANUFACTURER;
        return !TextUtils.isEmpty(manufacturer) && manufacturer.contains("HUAWEI");
    }

    /**
     * 获取华为EmotionUI版本号，失败默认返回-1
     */
    public static double getEmotionUiVersion() {
        try {
            String emuiVersion = getSystemProperty("ro.build.version.emui");
            String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
            return Double.parseDouble(version);
        } catch (Exception e) {
            Log.e(TAG, "get emui version code error");
        }
        return -1;
    }

    /**
     * 判断是否为小米系统
     */
    public static boolean isMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    /**
     * 获取小米MIUI版本号，失败默认返回-1
     */
    public static int getMiuiVersion() {
        String version = getSystemProperty("ro.miui.ui.version.name");
        if (TextUtils.isEmpty(version)) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                Log.e(TAG, "get miui version code error");
            }
        }
        return -1;
    }

    /**
     * 判断是否为魅族系统
     */
    public static boolean isMeizuRom() {
        String meizuFlymeOSFlag = getSystemProperty("ro.build.display.id");
        return !TextUtils.isEmpty(meizuFlymeOSFlag) && meizuFlymeOSFlag.toLowerCase().contains("flyme");
    }

    /**
     * 判断是否为360系统
     */
    public static boolean checkIs360Rom() {
        String manufacturer = Build.MANUFACTURER;
        return !TextUtils.isEmpty(manufacturer) && manufacturer.contains("QiKU");
    }

    /**
     * 判断是否为乐视系统
     */
    public static boolean isLetvRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.letv.eui"));
    }

    /**
     * 判断是否为Oppo系统
     */
    public static boolean isOppoRom() {
        String a = getSystemProperty("ro.product.brand");
        return !TextUtils.isEmpty(a) && a.toLowerCase().contains("oppo");
    }

    /**
     * 判断是否为Vivo系统
     */
    public static boolean isVivoRom() {
        String a = getSystemProperty("ro.vivo.os.name");
        return !TextUtils.isEmpty(a) && a.toLowerCase().contains("funtouch");
    }

    /**
     * 判断是否为联想系统
     */
    public static boolean isLenovoRom() {
        String fingerPrint = Build.FINGERPRINT;
        if (!TextUtils.isEmpty(fingerPrint)) {
            return fingerPrint.contains("VIBEUI_V2");
        }
        String a = getSystemProperty("ro.build.version.incremental");
        return !TextUtils.isEmpty(a) && a.contains("VIBEUI_V2");
    }

    /**
     * 判断是否为CoolPad系统
     */
    public static boolean isCoolPadRom() {
        String model = Build.MODEL;
        String fingerPrint = Build.FINGERPRINT;
        return (!TextUtils.isEmpty(model) && model.toLowerCase().contains("coolpad"))
                || (!TextUtils.isEmpty(fingerPrint) && fingerPrint.toLowerCase().contains("coolpad"));
    }

    /**
     * 判断是否为中兴系统
     */
    public static boolean isZTERom() {
        String manufacturer = Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)) {
            return manufacturer.toLowerCase().contains("nubia") || manufacturer.toLowerCase().contains("zte");
        }
        String fingerPrint = Build.FINGERPRINT;
        if (!TextUtils.isEmpty(fingerPrint)) {
            return fingerPrint.toLowerCase().contains("nubia") || fingerPrint.toLowerCase().contains("zte");
        }
        return false;
    }

    public static boolean isDomesticSpecialRom() {
        return RomUtils.isMiuiRom()
                || RomUtils.isHuaweiRom()
                || RomUtils.isMeizuRom()
                || RomUtils.checkIs360Rom()
                || RomUtils.isOppoRom()
                || RomUtils.isVivoRom()
                || RomUtils.isLetvRom()
                || RomUtils.isLenovoRom()
                || RomUtils.isCoolPadRom();
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 360权限申请
     */
    public static void apply360Permission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 小米权限申请
     */
    public static void applyMiuiPermission(Context context) {
        Intent intent = new Intent();
        intent.setAction("miui.intent.action.APP_PERM_EDITOR");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 魅族权限申请
     */
    public static void applyMeizuPermission(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 华为权限申请
     */
    public static void applyHuaweiPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
            intent.setComponent(comp);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                comp = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
                intent.setComponent(comp);
                context.startActivity(intent);
            }
        } catch (SecurityException e) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.Android.settings", "com.android.settings.permission.TabItem");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * CoolPad权限申请
     */
    public static void applyCoolpadPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.yulong.android.seccenter", "com.yulong.android.seccenter.dataprotection.ui.AppListActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 联想权限申请
     */
    public static void applyLenovoPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.lenovo.safecenter", "com.lenovo.safecenter.MainTab.LeSafeMainActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 中兴权限申请
     */
    public static void applyZTEPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("com.zte.heartyservice.intent.action.startActivity.PERMISSION_SCANNER");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 乐视权限申请
     */
    public static void applyLetvPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AppActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Vivo权限申请
     */
    public static void applyVivoPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.FloatWindowManager");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Oppo权限申请
     */
    public static void applyOppoPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

}