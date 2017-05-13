package cn.tonlyshy.app.fmpet.core;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.tonlyshy.app.fmpet.R;
import cn.tonlyshy.app.fmpet.view.BubbleView;
import cn.tonlyshy.app.fmpet.view.FloatViewGroup;

/**
 * Created by Liaowm5 on 2017/5/13.
 */

public class FloatViewManager {
    private Context context;
    private WindowManager windowManager;
    private FloatViewGroup floatViewGroup;
    private BubbleView bubbleView;
    WindowManager.LayoutParams bubbleParams;
    WindowManager.LayoutParams params;
    private LinearLayout rightLayout;
    private LinearLayout leftLayout;
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
                    bubbleParams.x = params.x + params.width;
                    bubbleParams.y = params.y;
                    windowManager.updateViewLayout(floatViewGroup,params);
                    windowManager.updateViewLayout(bubbleView, bubbleParams);
                    startx=x;
                    starty=y;
                    break;
                case MotionEvent.ACTION_UP://贴边
                    floatViewGroup.setDragState(false);
                    float x1=event.getRawX();
                    leftLayout = (LinearLayout) bubbleView.findViewById(R.id.left_layout);
                    rightLayout = (LinearLayout) bubbleView.findViewById(R.id.right_layout);

                    if(x1>getScreenWidth()/2){
                        params.x=getScreenWidth()-floatViewGroup.width;
                        leftLayout.setVisibility(View.VISIBLE);
                        rightLayout.setVisibility(View.GONE);
                        bubbleParams.x = params.x - bubbleParams.width;
                        windowManager.updateViewLayout(bubbleView, bubbleParams);
                    }else{
                        params.x=0;
                        leftLayout.setVisibility(View.GONE);
                        rightLayout.setVisibility(View.VISIBLE);
                        bubbleParams.x = params.x + params.width;
                        windowManager.updateViewLayout(bubbleView, bubbleParams);
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
        bubbleView=new BubbleView(context);

        ViewGroup.LayoutParams paramsBubble=bubbleView.getLayoutParams();

        bubbleParams =new WindowManager.LayoutParams();
        bubbleParams.width=floatViewGroup.width;
        bubbleParams.height=floatViewGroup.height;
        bubbleParams.gravity= Gravity.TOP|Gravity.LEFT;
        bubbleParams.x= bubbleParams.x+floatViewGroup.width;
        bubbleParams.y= bubbleParams.y+floatViewGroup.height;
        bubbleParams.type= WindowManager.LayoutParams.TYPE_PHONE;
        bubbleParams.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        bubbleParams.format= PixelFormat.RGBA_8888;

        windowManager.addView(bubbleView, bubbleParams);

    }

    public void setBubbleViewText(String text) {
        leftLayout = (LinearLayout) bubbleView.findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) bubbleView.findViewById(R.id.right_layout);
        TextView rightMsg = (TextView) rightLayout.findViewById(R.id.right_msg);
        TextView leftMsg = (TextView) leftLayout.findViewById(R.id.left_msg);
        rightMsg.setText(text);
        leftMsg.setText(text);
        windowManager.updateViewLayout(bubbleView, bubbleParams);
    }
}
