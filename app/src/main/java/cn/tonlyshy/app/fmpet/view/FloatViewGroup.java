package cn.tonlyshy.app.fmpet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.tonlyshy.app.fmpet.R;


/**
 * Created by Liaowm5 on 2017/5/13.
 */

public class FloatViewGroup extends RelativeLayout {
    public int width=500;
    public int height=500;
    private boolean isDragging=false;
    public int circleRadius=150;
    View view;

    Paint circlePaint;
    Paint textPaint;

    String text="50%";

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

    private void initPaints() {
        circlePaint=new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);

        textPaint=new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        view= LayoutInflater.from(getContext()).inflate(R.layout.test,null);
        this.addView(view);
        setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isDragging){
            Bitmap src= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher );
            Bitmap bitmap=Bitmap.createScaledBitmap(src,width,height,true);
            canvas.drawBitmap(bitmap,0,0,null);
        } else {
            canvas.drawCircle(width / 2, height / 2, circleRadius, circlePaint);

            float textWidth = textPaint.measureText(text);
            float x = width / 2 - textWidth / 2;

            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            float y = height / 2 + dy;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public void setDragState(boolean flag){
        isDragging=flag;
        invalidate();
    }
}
