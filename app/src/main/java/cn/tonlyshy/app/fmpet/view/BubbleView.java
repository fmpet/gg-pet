package cn.tonlyshy.app.fmpet.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.tonlyshy.app.fmpet.R;

/**
 * Created by liaowm5 on 17/5/14.
 */

public class BubbleView extends LinearLayout {

    public int width = 400;
    public int height = 200;

    View view;

    private LinearLayout rightLayout;
    private LinearLayout leftLayout;

    public BubbleView(Context context) {
        super(context);
        initialBubble();
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialBubble();
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialBubble();
    }

    public void initialBubble() {
        view= LayoutInflater.from(getContext()).inflate(R.layout.right_msg,null);
        this.addView(view);
        setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setBubbleViewText(final String text, WindowManager windowManager, WindowManager.LayoutParams bubbleParams) {
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);


        leftLayout.post(new Runnable() {
            @Override
            public void run() {
//                Looper.prepare();
                TextView leftMsg = (TextView) leftLayout.findViewById(R.id.left_msg);
                leftMsg.setText(text);
            }
        });

        rightLayout.post(new Runnable() {
            @Override
            public void run() {
//                Looper.prepare();
                TextView rightMsg = (TextView) rightLayout.findViewById(R.id.right_msg);
                rightMsg.setText(text);
            }
        });

//        windowManager.updateViewLayout(this, bubbleParams);
    }
}
