package cn.tonlyshy.app.fmpet.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cn.tonlyshy.app.fmpet.R;

/**
 * Created by liaowm5 on 17/5/14.
 */

public class BubbleView extends LinearLayout {

    View view;
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
}
