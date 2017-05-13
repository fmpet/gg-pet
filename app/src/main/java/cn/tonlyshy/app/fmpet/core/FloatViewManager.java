package cn.tonlyshy.app.fmpet.core;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Toast;

import cn.tonlyshy.app.fmpet.view.BubbleView;
import cn.tonlyshy.app.fmpet.view.FloatViewGroup;

/**
 * Created by Liaowm5 on 2017/5/13.
 */

public class FloatViewManager {
    private Context context;
    private WindowManager windowManager;
    private FloatViewGroup floatViewGroup;
    WindowManager.LayoutParams params;
    private View.OnTouchListener floatViewGroupTouchListener=new View.OnTouchListener() {
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
                    floatViewGroup.setDragState(true);
                    x=event.getRawX();
                    y=event.getRawY();
                    float dx=x-startx;
                    float dy=y-starty;
                    params.x+=dx;
                    params.y+=dy;
                    windowManager.updateViewLayout(floatViewGroup,params);
                    startx=x;
                    starty=y;
                    break;
                case MotionEvent.ACTION_UP://贴边
                    floatViewGroup.setDragState(false);
                    float x1=event.getRawX();
                    if(x1>getScreenWidth()/2){
                        params.x=getScreenWidth()-floatViewGroup.width;
                    }else{
                        params.x=0;
                    }
                    windowManager.updateViewLayout(floatViewGroup,params);
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
        floatViewGroup=new FloatViewGroup(context);
        floatViewGroup.setOnTouchListener(floatViewGroupTouchListener);
        floatViewGroup.setOnClickListener(new View.OnClickListener() {
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
    public void showfloatViewGroup(){
        params=new WindowManager.LayoutParams();
        params.width=floatViewGroup.width;
        params.height=floatViewGroup.height;
        params.gravity= Gravity.TOP|Gravity.LEFT;
        params.x=0;
        params.y=0;
        params.type= WindowManager.LayoutParams.TYPE_PHONE;
        params.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format= PixelFormat.RGBA_8888;
        windowManager.addView(floatViewGroup,params);
        showRightMessage();
    }

    public void showRightMessage(){
        BubbleView bubbleView=new BubbleView(context);

        ViewGroup.LayoutParams paramsBubble=bubbleView.getLayoutParams();
        WindowManager.LayoutParams params0;
        params0=new WindowManager.LayoutParams();
        params0.width=floatViewGroup.width;
        params0.height=floatViewGroup.height;
        params0.gravity= Gravity.TOP|Gravity.LEFT;
        params0.x=params0.x+floatViewGroup.width;
        params0.y=params0.y+floatViewGroup.height;
        params0.type= WindowManager.LayoutParams.TYPE_PHONE;
        params0.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params0.format= PixelFormat.RGBA_8888;

        windowManager.addView(bubbleView,params0);

    }
}
