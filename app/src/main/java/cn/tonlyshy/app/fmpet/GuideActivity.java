package cn.tonlyshy.app.fmpet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import cn.tonlyshy.app.fmpet.utility.PermissionCheckerer;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

    Switch guideSwitch1;
    Switch guideSwitch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(permissionCheck()) {
            Intent intent = new Intent(GuideActivity.this, Main2Activity.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_guide);
        getSupportActionBar().hide();

        guideSwitch1=(Switch)findViewById(R.id.switch_guide_step1);
        guideSwitch2=(Switch)findViewById(R.id.switch_guide_step2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (permissionCheck()!=true){
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(GuideActivity.this,Main2Activity.class);
                        startActivity(intent);
                        GuideActivity.this.finish();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionCheckerer.checkFloatWindowPermission(this)!=true){
            guideSwitch1.setChecked(false);
        }else{
            guideSwitch1.setChecked(true);
        }
        if(PermissionCheckerer.checkNotificationListenerPermission(this)!=true){
            guideSwitch2.setChecked(false);
        }else{
            guideSwitch2.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_guide_step1:
                if(PermissionCheckerer.checkFloatWindowPermission(this)!=true){
                    PermissionCheckerer.requestFloatWindowPermission(this);
                }else {
                    Toast.makeText(this,"已启用",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_guide_step2:
                if(PermissionCheckerer.checkNotificationListenerPermission(this)!=true){
                    PermissionCheckerer.requestNotificationListenerPermission(this);
                }else {
                    Toast.makeText(this,"已启用",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean permissionCheck(){
        if (PermissionCheckerer.checkFloatWindowPermission(this)!=true||PermissionCheckerer.checkNotificationListenerPermission(this)!= true){
            return false;
        }
        return true;
    }
}
