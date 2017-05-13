package cn.tonlyshy.app.fmpet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import cn.tonlyshy.app.fmpet.R;


/**
 * Created by Liaowm5 on 2017/5/13.
 */

public class FloatCircleView extends View {
    public int width=150;
    public int height=150;
    private boolean isDragging=false;

    Paint circlePaint;
    Paint textPaint;

    String text="50%";
    public FloatCircleView(Context context) {
        super(context);
        initPaints();
    }

    public FloatCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isDragging){
            Bitmap src= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher );
            Bitmap bitmap=Bitmap.createScaledBitmap(src,width,height,true);
            canvas.drawBitmap(bitmap,0,0,null);
        } else {
            canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);

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
