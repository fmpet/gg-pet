package cn.tonlyshy.app.fmpet.core;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import cn.tonlyshy.app.fmpet.view.FloatCircleView;

/**
 * Created by Liaowm5 on 2017/5/13.
 */

public class FloatViewManager {
    private Context context;
    private WindowManager windowManager;
    private FloatCircleView floatCircleView;
    WindowManager.LayoutParams params;
    private View.OnTouchListener floatCircleViewTouchListener=new View.OnTouchListener() {
        float startx;
        float starty;
        float x;
        float y;
        float x0;
        float y0;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startx=event.getRawX();
                    starty=event.getRawY();
                    x0=startx;
                    y0=starty;
                    break;
                case MotionEvent.ACTION_MOVE:
                    floatCircleView.setDragState(true);
                    x=event.getRawX();
                    y=event.getRawY();
                    float dx=x-startx;
                    float dy=y-starty;
                    params.x+=dx;
                    params.y+=dy;
                    windowManager.updateViewLayout(floatCircleView,params);
                    startx=x;
                    starty=y;
                    break;
                case MotionEvent.ACTION_UP://贴边
                    floatCircleView.setDragState(false);
                    float x1=event.getRawX();
                    if(x1>getScreenWidth()/2){
                        params.x=getScreenWidth()-floatCircleView.width;
                    }else{
                        params.x=0;
                    }
                    windowManager.updateViewLayout(floatCircleView,params);
                    if(Math.abs(x0-x1)>6){//px
                        return true;
                    }else{
                        return false;
                    }
            }
            return false;
        }
    };

    private FloatViewManager(final Context context){
        this.context=context;
        windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        floatCircleView=new FloatCircleView(context);
        floatCircleView.setOnTouchListener(floatCircleViewTouchListener);
        floatCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"FloatPet",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static FloatViewManager instance;

    public static FloatViewManager getInstance(Context context){
        if(instance==null){
            synchronized (FloatViewManager.class){
                if(instance==null){
                    instance=new FloatViewManager(context);
                }
            }
        }
        return instance;
    }

    public int getScreenWidth(){
        return windowManager.getDefaultDisplay().getWidth();
    }

    /*
    *  展示到window
    * */
    public void showFloatCircleView(){
        params=new WindowManager.LayoutParams();
        params.width=floatCircleView.width;
        params.height=floatCircleView.height;
        params.gravity= Gravity.TOP|Gravity.LEFT;
        params.x=0;
        params.y=0;
        params.type= WindowManager.LayoutParams.TYPE_PHONE;
        params.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format= PixelFormat.RGBA_8888;
        windowManager.addView(floatCircleView,params);
    }
}
