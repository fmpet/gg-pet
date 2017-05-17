package cn.tonlyshy.app.fmpet.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.tonlyshy.app.fmpet.R;
import cn.tonlyshy.app.fmpet.view.BubbleView;
import cn.tonlyshy.app.fmpet.view.FloatViewGroup;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

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
    private boolean ready=false;

    private String petName="滑鸡";

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
                    bubbleParams.y = params.y - params.height;
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


    private int index=0;

    private FloatViewManager(final Context context){
        this.context=context;
        windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        floatViewGroup=new FloatViewGroup(context);
        floatViewGroup.setOnTouchListener(floatViewGroupTouchListener);
        floatViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if(index>=floatViewGroup.animationList.size()){
                    index=0;
                }
                floatViewGroup.switchAnimation(index);
                Log.d(TAG, "onClick: index="+index);
                Toast.makeText(context,"FloatPet",Toast.LENGTH_SHORT).show();
            }
        });
        ready=false;
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

        floatViewGroup.startAnime();
        showRightMessage();
        ready=true;
        leftLayout = (LinearLayout) bubbleView.findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) bubbleView.findViewById(R.id.right_layout);
    }


    public void showRightMessage(){
        bubbleView=new BubbleView(context);
        bubbleParams =new WindowManager.LayoutParams();
        bubbleParams.width=bubbleView.width;
        bubbleParams.height=bubbleView.height;
        bubbleParams.gravity= Gravity.TOP|Gravity.LEFT;
        bubbleParams.x= bubbleParams.x+floatViewGroup.width;
        bubbleParams.y= floatViewGroup.height;
        bubbleParams.type= WindowManager.LayoutParams.TYPE_PHONE;
        bubbleParams.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        bubbleParams.format= PixelFormat.RGBA_8888;
        bubbleView.setInvisible();
        windowManager.addView(bubbleView, bubbleParams);
        setBubbleViewText("Hi, Master!", null);
    }

    public void removeView() {
        windowManager.removeView(bubbleView);
        windowManager.removeView(floatViewGroup);
    }

    public void setBubbleViewText(String text, Object icon) {
        bubbleView.setVisible();
        Bitmap bitmap=(Bitmap)icon;
        bubbleView.setBubbleViewText(text, windowManager, bubbleParams,bitmap);
        float scale = context.getResources().getDisplayMetrics().density;
        float wordSize = scale * 14 + 0.5f;
        bubbleView.width = (int) wordSize * (text.length() + 2);
        bubbleParams.width = bubbleView.width > (windowManager.getDefaultDisplay().getWidth() - wordSize * 4) ?
                (int) (windowManager.getDefaultDisplay().getWidth() - wordSize * 4) : bubbleView.width;
        windowManager.updateViewLayout(bubbleView, bubbleParams);
        bubbleView.setInvisibleDelayed(3000);
        startWalk();
    }

    public boolean isReady() {
        return ready&&bubbleView!=null;
    }


    Handler mHanlder=new Handler();

    Runnable update_thread = new Runnable()
    {
        public void run()
        {
            params.x+=flagx;
            params.y+=flagy;
            if(params.x>=windowManager.getDefaultDisplay().getWidth())
                flagx=-flag;
            if(params.x<=0)
                flagx=flag;

            if(params.y>=windowManager.getDefaultDisplay().getHeight())
                flagy=-flag;
            if(params.y<=0)
                flagy=flag;

            try{
            windowManager.updateViewLayout(floatViewGroup,params);
            }catch (Exception e){
                e.printStackTrace();
            }
            //Unhandled Exception




            bubbleParams.x = params.x + params.width;
            bubbleParams.y = params.y - params.height;
            if(params.x>getScreenWidth()/2){
                leftLayout.setVisibility(View.VISIBLE);
                rightLayout.setVisibility(View.GONE);
            }else{
                leftLayout.setVisibility(View.GONE);
                rightLayout.setVisibility(View.VISIBLE);
            }
            try{
                windowManager.updateViewLayout(bubbleView, bubbleParams);
            }catch (Exception e){
                e.printStackTrace();
            }
            //Unhandled Exception
            mHanlder.postDelayed(update_thread, 16);

        }
    };
    int flag=1;
    int flagx=flag;
    int flagy=flag;
    public void startWalk() {
        mHanlder.post(update_thread);
        Log.d(TAG, "startWalk: ");
        floatViewGroup.startWalkAnimation();
    }

    public void stopWalk(){
        mHanlder.removeCallbacks(update_thread);
        floatViewGroup.stopWalkAnimation();
    }


    public void setPetName(String petName){
        this.petName=petName;
        SharedPreferences.Editor editor=context.getSharedPreferences("petName",MODE_PRIVATE).edit();
        editor.putString("petName",petName);
    }

    public String getPetName(){
        return petName;
    }

}
