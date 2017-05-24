package cn.tonlyshy.app.fmpet;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cn.tonlyshy.app.fmpet.core.FloatViewManager;
import cn.tonlyshy.app.fmpet.fragment.AboutUsFragment;
import cn.tonlyshy.app.fmpet.fragment.ClockFragment;
import cn.tonlyshy.app.fmpet.fragment.MainFragment;
import cn.tonlyshy.app.fmpet.fragment.ModelChangeFragment;
import cn.tonlyshy.app.fmpet.utility.PermissionCheckerer;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SYSTEM_ALERT_WINDOW_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //权限申请
        new Thread(new Runnable() {
            @Override
            public void run() {
                permissionCheck();
            }
        }).start();

        //test
        toggleNotificationListenerService();//Prevent start avtivity the second time NotificationMonitor can not get notifications
        permissionCheck();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

        SharedPreferences preferences=getSharedPreferences("petName",MODE_PRIVATE);
        if(preferences!=null){
            String petName=preferences.getString("petName","liaowm5%$%^^%$#");
            if(petName!=null&&!petName.equals("liaowm5%$%^^%$#")){
                FloatViewManager floatViewManager=FloatViewManager.getInstance(this);
                floatViewManager.setPetName(petName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new Fragment();
        if (id == R.id.nav_main_page) {
            // Handle the camera action
            fragment=new MainFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_model_select) {
            fragment=new ModelChangeFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_clock) {
            //fragment=new ClockFragment();
            DialogFragment fragment1 = new ClockFragment();
            try {
                fragment1.show(getSupportFragmentManager(), "tag");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_setting) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
            fragment = new AboutUsFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }else if(id==R.id.nav_exit){
            stopService(new Intent(this,MyFloatService.class));
            finish();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


}
