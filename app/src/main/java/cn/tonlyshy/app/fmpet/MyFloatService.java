package cn.tonlyshy.app.fmpet;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import cn.tonlyshy.app.fmpet.core.FloatViewManager;

/**
 * Created by Liaowm5 on 2017/5/13.
 */

public class MyFloatService extends Service {

    FloatViewManager floatViewManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        floatViewManager=FloatViewManager.getInstance(this);
        floatViewManager.showfloatViewGroup();
        mHandler.post(walk_thread);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        FloatViewManager floatViewManager=FloatViewManager.getInstance(this);
        floatViewManager.stopWalk();
        floatViewManager.removeView();
        super.onDestroy();
    }

    Handler mHandler= new Handler();

    private boolean isWalking=false;

    private long walkTimeInMillis=5000;

    private long stopTimeInMillis=10000;

    Runnable walk_thread =new Runnable(){
        @Override
        public void run() {
            if(isWalking){
                isWalking=false;
                floatViewManager.stopWalk();
                mHandler.postDelayed(walk_thread,stopTimeInMillis);
            }else{
                isWalking=true;
                floatViewManager.startWalk();
                mHandler.postDelayed(walk_thread,walkTimeInMillis);
            }
        }
    };

}
