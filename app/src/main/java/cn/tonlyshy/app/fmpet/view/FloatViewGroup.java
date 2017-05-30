package cn.tonlyshy.app.fmpet.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import cn.tonlyshy.app.fmpet.R;


/**
 * Created by Liaowm5 on 2017/5/13.
 */

public class FloatViewGroup extends RelativeLayout {
    public int width=200;
    public int height=200;
    private boolean isDragging=false;
    public int circleRadius=100;

    Paint circlePaint;
    Paint textPaint;

    String text="50%";

    /*
    *  MultiApperance
    *
    * */
    int apperanceId = 0; //0 1 2
    boolean isWalking=false;
    int animeIndex = 0;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public FloatViewGroup(Context context) {
        super(context);
        initPaints();
    }

    public FloatViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public FloatViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private AnimationDrawable animationDrawable;
    public ArrayList< ArrayList<Integer> > animationListList=new ArrayList< ArrayList<Integer> >();
    public ArrayList<Integer> animationList=new ArrayList<>();
    public ArrayList<Integer> walkAnimationList=new ArrayList<>();

    private void initPaints() {
        circlePaint=new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);

        textPaint=new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        animationList.add(R.drawable.a1_animation_list_happy);
        animationList.add(R.drawable.a1_animation_list_sad);
        animationList.add(R.drawable.a1_animation_list_jiong);
        animationList.add(R.drawable.a1_animation_list_silly);
        animationListList.add(animationList);

        animationList = new ArrayList<>();
        animationList.add(R.drawable.a2_animation_list_happy);
        animationList.add(R.drawable.a2_animation_list_sad);
        animationList.add(R.drawable.a2_animation_list_jiong);
        animationList.add(R.drawable.a2_animation_list_silly);
        animationListList.add(animationList);

        animationList = new ArrayList<>();
        animationList.add(R.drawable.a3_animation_list_happy);
        animationList.add(R.drawable.a3_animation_list_sad);
        animationList.add(R.drawable.a3_animation_list_jiong);
        animationList.add(R.drawable.a3_animation_list_silly);
        animationListList.add(animationList);

        walkAnimationList.add(R.drawable.a1_animation_list_walk);
        walkAnimationList.add(R.drawable.a2_animation_list_walk);
        walkAnimationList.add(R.drawable.a3_animation_list_walk);

        //setBackground(new ColorDrawable(Color.TRANSPARENT));
        setBackgroundResource(animationList.get(apperanceId));
        animationDrawable=(AnimationDrawable) getBackground();
        paint.setAntiAlias(false);
        paint.setDither(true);
        paint.setFilterBitmap(false);
    }
    Paint paint=new Paint();

/*
    int xx=0;
    int yy=0;
    @Override
    protected void onDraw(Canvas canvas) {
        if(!isDragging){
            //Bitmap src= BitmapFactory.decodeResource(getResources(), R.drawable.kanna_body );
            //canvas.scale(6, 6);
            //canvas.drawBitmap(src,0,0,paint);
            //super.onDraw(canvas);
        } else {
            canvas.drawCircle(width / 2, height / 2, circleRadius, circlePaint);

            float textWidth = textPaint.measureText(text);
            //float x = width / 2 - textWidth / 2;

            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            //float y = height / 2 + dy;
            canvas.drawText(text, ++xx, ++yy, textPaint);
            //setX(xx);
            //setY(yy);
            //scrollTo(xx,yy);
        }
    }
   */
    Random random=new Random();
    public void switchAnimation(){
        animeIndex=random.nextInt(animationListList.get(apperanceId).size());
        if(!isWalking) {
            animationDrawable.stop();
            setBackgroundResource(animationListList.get(apperanceId).get(animeIndex));
            animationDrawable = (AnimationDrawable) getBackground();
            animationDrawable.start();
        }
    }

    public void stopAnimation(){
        animationDrawable.stop();
    }

    public void setDragState(boolean flag){
        isDragging=flag;
        invalidate();
    }

    public void startAnime(){
        animationDrawable.start();
    }

    public void startWalkAnimation() {
        isWalking=true;
        animationDrawable.stop();
        setBackgroundResource(walkAnimationList.get(apperanceId));
        animationDrawable=(AnimationDrawable)getBackground();
        animationDrawable.start();
    }
    public void stopWalkAnimation() {
        animationDrawable.stop();
        setBackgroundResource(animationListList.get(apperanceId).get(animeIndex));
        animationDrawable=(AnimationDrawable)getBackground();
        isWalking=false;
        animationDrawable.start();
    }

    public void setApperance(int id){
        apperanceId=id;
        animationDrawable.stop();
        if(isWalking){
            setBackgroundResource(walkAnimationList.get(apperanceId));
        }else{
            setBackgroundResource(animationListList.get(apperanceId).get(animeIndex));
        }
        animationDrawable=(AnimationDrawable)getBackground();
        animationDrawable.start();
    }

    public int getApperanceId(){
        return apperanceId;
    }
}
