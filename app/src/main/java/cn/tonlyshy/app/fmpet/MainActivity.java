package cn.tonlyshy.app.fmpet;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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
        permissionCheck();
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
    public void permissionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10);
            } else {
                isPermmited = true;
            }
            if (isPermmited)
                startServicef();
        }
    }

    public void toggleNotificationListenerService(){
        PackageManager pm=getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this,cn.tonlyshy.app.fmpet.plugin.NotificationMonitor.class),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this,cn.tonlyshy.app.fmpet.plugin.NotificationMonitor.class),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_show:
                permissionCheck();
            break;
        }
    }

    public void startServicef(){
        Intent intent=new Intent(this,MyFloatService.class);
        startService(intent);
    }
}
