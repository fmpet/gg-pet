package cn.tonlyshy.app.fmpet;

import android.app.Service;
import android.content.Intent;
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
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        FloatViewManager floatViewManager=FloatViewManager.getInstance(this);
        floatViewManager.removeView();
        super.onDestroy();
    }

}
