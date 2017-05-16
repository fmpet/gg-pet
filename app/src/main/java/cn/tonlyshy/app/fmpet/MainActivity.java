package cn.tonlyshy.app.fmpet;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import cn.tonlyshy.app.fmpet.utility.PermissionCheckerer;

public class MainActivity extends AppCompatActivity {
    private boolean isPermmited=false;
    private static final int SYSTEM_ALERT_WINDOW_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //权限申请
        new Thread(new Runnable() {
            @Override
            public void run() {
                permissionCheck();
            }
        }).start();
        toggleNotificationListenerService();//Prevent start avtivity the second time NotificationMonitor can not get notifications
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == SYSTEM_ALERT_WINDOW_REQUEST_CODE) {
                if (Settings.canDrawOverlays(this)) {
                    Log.i("MainActivity", "onActivityResult granted");
                }
            }
        }
    }
    private boolean permissionCheck(){
        if (PermissionCheckerer.checkFloatWindowPermission(this)!=true||PermissionCheckerer.checkNotificationListenerPermission(this)!= true){
            if(PermissionCheckerer.checkFloatWindowPermission(this)!=true){
                PermissionCheckerer.requestFloatWindowPermission(this);
            }
            if(PermissionCheckerer.checkNotificationListenerPermission(this)!= true){
                PermissionCheckerer.requestNotificationListenerPermission(this);
            }
        }
        return true;
    }

    public void toggleNotificationListenerService(){
        PackageManager pm=getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this,cn.tonlyshy.app.fmpet.plugin.NotificationMonitor.class),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this,cn.tonlyshy.app.fmpet.plugin.NotificationMonitor.class),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_show:
                toggleNotificationListenerService();
                if(permissionCheck()){
                    startServicef();
                }
                break;
            case R.id.btn_close:
                stopService(new Intent(this,MyFloatService.class));
                break;
            case R.id.btn_quit:
                stopService(new Intent(this,MyFloatService.class));
                finish();
                break;
        }
    }

    public void startServicef(){
        Intent intent=new Intent(this,MyFloatService.class);
        startService(intent);
    }
}
